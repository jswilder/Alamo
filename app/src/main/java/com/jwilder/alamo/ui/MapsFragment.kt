package com.jwilder.alamo.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jwilder.alamo.R
import com.jwilder.alamo.viewmodel.VenuesSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {

    val viewModel: VenuesSharedViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.addMarkersFromVM()
        googleMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    austin,
                    10f,
                    0f,
                    0f
                )
            )
        )
    }

    private fun GoogleMap.addMarkersFromVM() {
        this.clear()
        viewModel.venueList.value?.forEach {
            // TODO: Need an onClick
            this.addMarker(MarkerOptions().position(LatLng(it.location.lat, it.location.lng)))
        }
    }

    companion object {
        val austin = LatLng(30.2672, -97.7431)
    }
}