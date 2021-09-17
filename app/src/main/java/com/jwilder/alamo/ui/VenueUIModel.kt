package com.jwilder.alamo.ui

import com.jwilder.alamo.remote.Category
import com.jwilder.alamo.remote.Location

data class VenueUIModel(
    val name: String,
    val id: String,
    val categories: List<Category> = emptyList(),
    val favorite: Boolean,
    val location: Location,
    val url: String
)
