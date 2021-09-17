package com.jwilder.alamo.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.jwilder.alamo.R
import com.jwilder.alamo.databinding.MarkerInfoContentsBinding
import com.jwilder.alamo.remote.Venue

class MapMarkerInfoAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker?): View? {
        val venue = marker?.tag as? Venue ?: return null
        val view = LayoutInflater.from(context).inflate(
            R.layout.marker_info_contents, null
        )
        view.findViewById<TextView>(R.id.venueName).apply {
            text = venue.name
            setOnClickListener {
                Log.d("JEREMY**", "Marker ${venue.name} + ${venue.id}")
            }
        }

        return view
    }

    override fun getInfoWindow(marker: Marker?): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }
}