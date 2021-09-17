package com.jwilder.alamo.modules

import android.app.Application
import androidx.room.Room
import com.jwilder.alamo.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "favorites_database"
        ).build()

    @Provides
    fun provides(db: AppDatabase) = db.venueDao()
}