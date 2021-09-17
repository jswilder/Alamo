package com.jwilder.alamo.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database model for a venue
 *
 * The only info we need to persist is the id
 */
@Entity(tableName = "favorites_database")
data class VenueEntity(
    @PrimaryKey
    val id: String
)
