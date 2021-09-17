package com.jwilder.alamo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jwilder.alamo.dao.VenueDao
import com.jwilder.alamo.repository.VenueEntity

@Database(entities = [VenueEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
}