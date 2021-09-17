package com.jwilder.alamo.repository

import com.jwilder.alamo.dao.VenueDao
import com.jwilder.alamo.remote.VenuesWebService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class VenuesRepository @Inject constructor(
    private val venueDao: VenueDao
) {

    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build();

    val service: VenuesWebService = retrofit.create(VenuesWebService::class.java)

    /**
     * TODO: Implement CRUD ops :: Map to UI Model
     */

    companion object {
        private const val BASE_URL = "https://api.foursquare.com/v2/"
    }
}