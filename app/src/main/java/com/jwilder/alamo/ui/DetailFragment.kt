package com.jwilder.alamo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.bumptech.glide.Glide
import com.jwilder.alamo.R
import com.jwilder.alamo.databinding.FragmentDetailBinding
import com.jwilder.alamo.remote.Venue
import com.jwilder.alamo.viewmodel.VenuesSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    val viewModel: VenuesSharedViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        val black = requireContext().getColor(R.color.secondaryTextColor)
        val white = requireContext().getColor(R.color.primaryTextColor)
        binding.toolbarVenueTitle.apply {
            setCollapsedTitleTextColor(white)
            setExpandedTitleColor(black)
        }

        viewModel.selectedVenue.observe(viewLifecycleOwner, {
            binding.toolbarVenueTitle.title = it.name
            Glide.with(requireContext())
                .load("https://maps.googleapis.com/maps/api/staticmap?markers=30.2672%2c%20-97.7431${it.getFormattedLatLong()}&zoom=11&size=400x400&key=AIzaSyALaVo5R1EkQlEcI4NUpZ3aqVllcCp8teM")
                .into(binding.venueStaticMap)
            binding.venueName.text = it.toString()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Venue.getFormattedLatLong() =
        "&markers=${this.location.lat}%2c%20${this.location.lng}"
}