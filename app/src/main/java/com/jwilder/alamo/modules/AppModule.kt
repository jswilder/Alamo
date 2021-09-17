package com.jwilder.alamo.modules

import com.jwilder.alamo.dao.VenueDao
import com.jwilder.alamo.repository.VenuesRepository
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    public fun provideVenuesRepository(venueDAO: VenueDao) = VenuesRepository(venueDAO)

}