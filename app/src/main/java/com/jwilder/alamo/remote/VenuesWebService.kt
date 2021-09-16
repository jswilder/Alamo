package com.jwilder.alamo.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VenuesWebService {

    /**
     * [near] e.g. "Austin, +TX"
     * [searchTerm] e.g. "coffee"
     * [limit] e.g. "20", the number of results to return
     * [version] e.g. "20180323"
     */
    @GET("venues/search/")
    suspend fun getNearbyVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("near") near: String,
        @Query("query") searchTerm: String,
        @Query("limit") limit: Int,
        @Query("v") version: Int
    ): Response<ResponseWrapper>
}