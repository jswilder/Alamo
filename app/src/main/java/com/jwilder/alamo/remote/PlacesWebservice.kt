package com.jwilder.alamo.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesWebservice {

    @GET("venues/search/")
    suspend fun getNearbyPlaces(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("near") near: String,
        @Query("query") searchTerm: String,
        @Query("limit") limit: Int
    ): Response<PlacesResponseModel>
}