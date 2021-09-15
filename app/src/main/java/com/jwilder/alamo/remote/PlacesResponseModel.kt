package com.jwilder.alamo.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlacesResponseModel(
    val id: String,
    val name: String,
    val location: Location,
    val categories: List<Category> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Location(
    val address: String,
    val crossStreet: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val lat: Double,
    val lng: Double,
    val distance: Double
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
