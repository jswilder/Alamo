package com.jwilder.alamo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jwilder.alamo.repository.VenueEntity

@Dao
interface VenueDao {

    @Query("SELECT * FROM favorites_database")
    suspend fun getAll(): List<VenueEntity>

    @Query("SELECT * FROM favorites_database WHERE id LIKE :id LIMIT 1")
    suspend fun findById(id: String): VenueEntity

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(vararg venues: VenueEntity)

    @Delete
    suspend fun delete(venue: VenueEntity)
}