package com.jwilder.alamo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jwilder.alamo.remote.Venue
import com.jwilder.alamo.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val venueList: LiveData<List<Venue>>
        get() = _venueList
    private val _venueList = MutableLiveData<List<Venue>>()

    fun fetchNearbyPlaces(searchTerm: String) {
        scope.launch {
            try {
                val response = placesRepository.service.getNearbyPlaces(
                    "DVX5MDJMDM5BSMI0GNGVRPUVF3R00JEARTEBEFGP40ZXAHUD",
                    "V2VAMQKQWVDWNGJAOUG3XUQCMGAVK5P05MANPPMATKKHLRTV",
                    "Austin,+TX",
                    searchTerm,
                    20,
                    20180323
                )
                if (response.isSuccessful) {
                    response.body()?.response?.venues?.let {
                        _venueList.postValue(it)
                        Log.d(TAG, it.toString())
                    } ?: run {
                        // TODO: Error state
                        Log.d(TAG, "Venues list is null")
                    }
                } else {
                    Log.d(TAG, "Response Failed")
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    companion object {
        const val TAG = "JEREMY**"
    }
}