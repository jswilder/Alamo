package com.jwilder.alamo.util

/**
 * Class to handle navigation events
 */
sealed class NavigationEvent {

    /**
     * NavigationEvent to represent navigation to the venue details fragment
     */
    object NavigateToVenueDetails : NavigationEvent()

    /**
     * NavigationEvent to represent navigation to the maps fragment
     */
    object NavigateToMapsFragment : NavigationEvent()
}
