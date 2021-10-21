package com.jwilder.alamo.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseWrapper(
    val response: Response
)

@JsonClass(generateAdapter = true)
data class Response(
    val venues: List<Venue>
)

@JsonClass(generateAdapter = true)
data class Venue(
    val id: String,
    val name: String,
    val location: Location,
    val categories: List<Category> = emptyList(),
    val delivery: Delivery?
)

@JsonClass(generateAdapter = true)
data class Location(
    val lat: Double,
    val lng: Double,
    val distance: Double? = 0.0,
    val address: String?
)

@JsonClass(generateAdapter = true)
data class Category(
    val id: String,
    val name: String,
    val icon: Icon,
    val primary: Boolean = false
)

@JsonClass(generateAdapter = true)
data class Icon(
    val prefix: String,
    val suffix: String
)

@JsonClass(generateAdapter = true)
data class Delivery(
    val url: String
)
