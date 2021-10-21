package com.jwilder.alamo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jwilder.alamo.repository.VenuesRepository
import com.jwilder.alamo.ui.VenueUIModel
import com.jwilder.alamo.util.NavigationEvent
import com.jwilder.alamo.util.SingleLiveEvent
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

    val venueList: LiveData<List<VenueUIModel>>
        get() = _venueList
    private val _venueList = MutableLiveData<List<VenueUIModel>>()

    val selectedVenue: LiveData<VenueUIModel>
        get() = _selectedVenue
    private val _selectedVenue = MutableLiveData<VenueUIModel>()

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
                _venueList.postValue(venuesRepository.getNearbyVenues(searchTerm))
                delay(DEBOUNCE_DELAY)
                debounceJob = null
            }
        }
    }

    fun toggleFavorite(venue: VenueUIModel) {
        scope.launch {
            if (venue.favorite) {
                venuesRepository.unfavorite(venue.id)
            } else {
                venuesRepository.favoriteVenue(venue.id)
            }
        }
    }

    /**
     * For clearing the LiveData when the search term is null or empty
     */
    fun clearVenuesList() {
        _venueList.postValue(emptyList())
    }

    fun navigateToVenueDetailsFragment(venue: VenueUIModel) {
        _selectedVenue.postValue(venue)
        _navigationSingleLiveEvent.postValue(NavigationEvent.NavigateToVenueDetails)
    }

    fun navigateToMapsFragment() {
        _navigationSingleLiveEvent.postValue(NavigationEvent.NavigateToMapsFragment)
    }

    companion object {
        private const val DEBOUNCE_DELAY = 250L
    }
}