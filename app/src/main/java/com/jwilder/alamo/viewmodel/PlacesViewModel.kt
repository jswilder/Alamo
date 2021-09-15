package com.jwilder.alamo.viewmodel

import androidx.lifecycle.ViewModel
import com.jwilder.alamo.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    val placesRepository: PlacesRepository
) : ViewModel() {

    val name = "Test Data for injection"
}