package com.jwilder.alamo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jwilder.alamo.util.NavigationEvent
import com.jwilder.alamo.remote.Venue
import com.jwilder.alamo.repository.VenuesRepository
import com.jwilder.alamo.util.SingleLiveEvent
import com.jwilder.alamo.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class VenuesSharedViewModel @Inject constructor(
    private val venuesRepository: VenuesRepository
) : ViewModel() {

    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val venueList: LiveData<List<Venue>>
        get() = _venueList
    private val _venueList = MutableLiveData<List<Venue>>()

    val selectedVenue: LiveData<Venue>
        get() = _selectedVenue
    private val _selectedVenue = MutableLiveData<Venue>()

    val navigationSingleLiveEvent: LiveData<NavigationEvent>
        get() = _navigationSingleLiveEvent
    private val _navigationSingleLiveEvent = SingleLiveEvent<NavigationEvent>()

    // A simple live data transformation to control the visibility of the map FAB if the list of
    // venues is null or empty
    val showMapFAB: LiveData<Boolean> = Transformations.map(venueList) {
        !it.isNullOrEmpty()
    }

    /**
     * job to track whether or not we have already made a fetch call within the debounce delay
     */
    private var debounceJob: Job? = null

    fun fetchNearbyVenues(searchTerm: String) {
        if (debounceJob == null) {
            debounceJob = scope.launch {
                try {
                    val response = venuesRepository.service.getNearbyVenues(
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
                            Log.d(TAG, "Venues list is null")
                        }
                    } else {
                        Log.d(TAG, "Response Failed")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
                delay(DEBOUNCE_DELAY)
                debounceJob = null
            }
        }
    }

    /**
     * For clearing the LiveData when the search term is null or empty
     */
    fun clearVenuesList() {
        _venueList.postValue(emptyList())
    }

    fun navigateToVenueDetailsFragment(venue: Venue) {
        _selectedVenue.postValue(venue)
        _navigationSingleLiveEvent.postValue(NavigationEvent.NavigateToVenueDetails)
    }

    fun navigateToMapsFragment() {
        _navigationSingleLiveEvent.postValue(NavigationEvent.NavigateToMapsFragment)
    }

    companion object {
        private val TAG = VenuesSharedViewModel::class.java.simpleName
        private const val DEBOUNCE_DELAY = 1_000L
    }
}