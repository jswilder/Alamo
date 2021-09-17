package com.jwilder.alamo

import com.jwilder.alamo.remote.Venue

/**
 * Class to handle navigation events
 */
sealed class NavigationEvent {

    /**
     * NavigationEvent to represent navigation to the venue details fragment
     */
    object NavigateToVenueDetails : NavigationEvent()
}
