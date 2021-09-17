package com.jwilder.alamo.repository

import com.jwilder.alamo.dao.VenueDao
import com.jwilder.alamo.remote.Venue
import com.jwilder.alamo.remote.VenuesWebService
import com.jwilder.alamo.ui.VenueUIModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class VenuesRepository {

    private val dao: VenueDao

    @Inject
    constructor(venueDao: VenueDao) {
        this.dao = venueDao
    }

    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build();

    private val service: VenuesWebService = retrofit.create(VenuesWebService::class.java)

    suspend fun getNearbyVenues(searchTerm: String): List<VenueUIModel> {
        return try {
            val response = service.getNearbyVenues(
                "DVX5MDJMDM5BSMI0GNGVRPUVF3R00JEARTEBEFGP40ZXAHUD",
                "V2VAMQKQWVDWNGJAOUG3XUQCMGAVK5P05MANPPMATKKHLRTV",
                "Austin,+TX",
                searchTerm,
                20,
                20180323
            )

            return if (!response.isSuccessful || response.body()?.response?.venues.isNullOrEmpty()) {
                emptyList()
            } else {
                val venues = response.body()?.response?.venues ?: emptyList()
                val favorites = dao.getAll()
                mapResponseToVenueUIModel(venues, favorites)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Adds the venue id to the favorites table
     */
    suspend fun favoriteVenue(id: String) {
        dao.insertAll(VenueEntity(id))
    }

    /**
     * Removes tjhe venue id from the favorites table
     */
    suspend fun unfavorite(id: String) {
        dao.delete(VenueEntity(id))
    }

    private fun mapResponseToVenueUIModel(
        venues: List<Venue>,
        favorites: List<VenueEntity>
    ): List<VenueUIModel> {
        // Convert favorites list to map for faster lookup
        val set = mutableSetOf<String>()
        for (favorite in favorites) {
            set.add(favorite.id)
        }

        return venues.map { venue ->
            VenueUIModel(
                name = venue.name,
                id = venue.id,
                categories = venue.categories,
                favorite = set.contains(venue.id),
                location = venue.location,
                url = venue.delivery?.url ?: ""
            )
        }
    }

    companion object {
        private const val BASE_URL = "https://api.foursquare.com/v2/"
    }
}