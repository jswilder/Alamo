package com.jwilder.alamo.ui

import com.jwilder.alamo.remote.Category

data class VenueUIModel(
    val name: String,
    val id: String,
    val categories: List<Category> = emptyList(),
    val favorite: Boolean,
    val url: String
)
