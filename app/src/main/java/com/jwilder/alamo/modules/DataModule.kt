package com.jwilder.alamo.modules

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jwilder.alamo.db.AppDatabase
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application, roomCallback: RoomDatabase.Callback) =
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "favorites_database"
        ).addCallback(roomCallback).build()

    @Provides
    fun provides(db: AppDatabase) = db.venueDao()
}