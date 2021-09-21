package com.jwilder.alamo.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.bumptech.glide.Glide
import com.jwilder.alamo.R
import com.jwilder.alamo.databinding.FragmentDetailBinding
import com.jwilder.alamo.viewmodel.VenuesSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.Int.Companion.MAX_VALUE

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
            buildDetailsList(it, container, inflater)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Builds the list of items to show for a venue's details
     */
    private fun buildDetailsList(
        venue: VenueUIModel,
        container: ViewGroup?,
        inflater: LayoutInflater
    ) {

        binding.favoriteIcon.setOnClickListener {
            favoriteButtonOnClick(venue)
            it.isEnabled = !it.isEnabled
        }

        if (venue.url.isNotBlank()) {
            val urlItem = getInflatedListItemView(
                getString(R.string.url),
                venue.url,
                container,
                inflater,
                true
            )
            urlItem.findViewById<TextView>(R.id.detailInfoUrl).setOnClickListener {
                urlOnClick(venue.url)
            }
            binding.venueDetailsContainer.addView(
                urlItem
            )
        }
        val catLabel = when (venue.categories.size) {
            1 -> getString(R.string.category)
            in 2..MAX_VALUE -> getString(R.string.categories)
            else -> null
        }
        catLabel?.let {
            binding.venueDetailsContainer.addView(
                getInflatedListItemView(it, venue.getCategoriesConcat(), container, inflater)
            )
        }

        /**
         * Adding dummy text to ensure there's enough detail to allow the collapsing toolbar to work
         */
        binding.venueDetailsContainer.addView(
            getInflatedListItemView(
                getString(R.string.lorem_ipsum_label),
                getString(R.string.lorem_ipsum),
                container,
                inflater
            )
        )
        binding.venueDetailsContainer.addView(
            getInflatedListItemView(
                getString(R.string.lorem_ipsum_label),
                getString(R.string.lorem_ipsum),
                container,
                inflater
            )
        )
    }

    /**
     * The onClick listener to be used for the favorite/unfavorite toggle
     */
    private fun favoriteButtonOnClick(venue: VenueUIModel) {
        viewModel.toggleFavorite(venue)
    }

    /**
     * The onClick listener used for opening an external browser
     */
    private fun urlOnClick(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun getInflatedListItemView(
        labelText: String,
        infoText: String,
        container: ViewGroup?,
        inflater: LayoutInflater,
        url: Boolean = false
    ) = if (url) {
        inflater.inflate(R.layout.venue_details_item_url, container, false).apply {
            findViewById<TextView>(R.id.detailItemLabel).text = labelText
            findViewById<TextView>(R.id.detailInfoUrl).text = infoText
        }
    } else {
        inflater.inflate(R.layout.venue_details_item, container, false).apply {
            findViewById<TextView>(R.id.detailItemLabel).text = labelText
            findViewById<TextView>(R.id.detailItemInfo).text = infoText
        }
    }

    private fun VenueUIModel.getFormattedLatLong() =
        "&markers=${this.location.lat}%2c%20${this.location.lng}"

    private fun VenueUIModel.getCategoriesConcat() =
        this.categories.joinToString(",", transform = { category -> category.name })
}