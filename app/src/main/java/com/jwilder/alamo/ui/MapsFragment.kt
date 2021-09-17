package com.jwilder.alamo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jwilder.alamo.R
import com.jwilder.alamo.util.NavigationEvent
import com.jwilder.alamo.viewmodel.VenuesSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.OnInfoWindowClickListener {

    val viewModel: VenuesSharedViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.navigationSingleLiveEvent.observe(viewLifecycleOwner, {
            when (it) {
                NavigationEvent.NavigateToVenueDetails -> {
                    findNavController().navigate(R.id.action_mapsFragment_to_detailFragment)
                }
            }
        })

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.addMarkersFromVM()
        googleMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    AUSTIN,
                    ZOOM_DEFAULT,
                    TILT_DEFAULT,
                    BEAR_DEFAULT
                )
            )
        )
    }

    private fun GoogleMap.addMarkersFromVM() {
        this.clear()
        viewModel.venueList.value?.forEach {
            val marker =
                this.addMarker(
                    MarkerOptions().position(LatLng(it.location.lat, it.location.lng))
                        .title(it.name)
                )
            marker?.tag = it
        }
    }

    /**
     * OnClick method for the map marker info window (venue name bubble)
     */
    override fun onInfoWindowClick(marker: Marker) {
        (marker.tag as? VenueUIModel)?.let {
            viewModel.navigateToVenueDetailsFragment(it)
        }
    }

    companion object {
        private val AUSTIN = LatLng(30.2672, -97.7431)
        private const val ZOOM_DEFAULT = 12f
        private const val TILT_DEFAULT = 0f
        private const val BEAR_DEFAULT = 0f
    }
}