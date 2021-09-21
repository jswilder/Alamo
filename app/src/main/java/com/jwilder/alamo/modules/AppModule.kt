package com.jwilder.alamo.modules

import com.jwilder.alamo.dao.VenueDao
import com.jwilder.alamo.repository.VenuesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideVenuesRepository(venueDao: VenueDao) = VenuesRepository(venueDao)
}