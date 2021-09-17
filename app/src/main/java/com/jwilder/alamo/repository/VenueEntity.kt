package com.jwilder.alamo.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database model for a venue
 *
 * The only info we need to persist is the id and if it has been favorited
 */
@Entity(tableName = "favorites_database")
data class VenueEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "favorite")
    val favorite: Boolean = false
)
