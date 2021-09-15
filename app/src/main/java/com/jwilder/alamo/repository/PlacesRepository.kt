package com.jwilder.alamo.repository

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jwilder.alamo.remote.PlacesWebservice
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class PlacesRepository @Inject constructor() {

    private val client: OkHttpClient = OkHttpClient().newBuilder().build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build();

    val service: PlacesWebservice = retrofit.create(PlacesWebservice::class.java)

    companion object {
        private const val BASE_URL = "https://api.foursquare.com/v2/"
    }
}