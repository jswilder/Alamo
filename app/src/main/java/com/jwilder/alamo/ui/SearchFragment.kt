package com.jwilder.alamo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jwilder.alamo.NavigationEvent
import com.jwilder.alamo.R
import com.jwilder.alamo.databinding.FragmentSearchBinding
import com.jwilder.alamo.remote.Venue
import com.jwilder.alamo.viewmodel.VenuesSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    val viewModel: VenuesSharedViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.venuesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VenueAdapter { item -> adapterOnClick(item) }
        }

        viewModel.venueList.observe(viewLifecycleOwner, { list ->
            (binding.venuesRecyclerView.adapter as VenueAdapter).updateData(list)
        })

        viewModel.showMapFAB.observe(viewLifecycleOwner, {
            binding.mapsFAB.isVisible = it
        })

        viewModel.navigationSingleLiveEvent.observe(viewLifecycleOwner, {
            when (it) {
                is NavigationEvent.NavigateToVenueDetails -> {
                    findNavController().navigate(R.id.action_SearchFragment_to_detailFragment)
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextListener(searchViewListener)

        binding.mapsFAB.setOnClickListener {
            findNavController().navigate(R.id.action_SearchFragment_to_mapsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun adapterOnClick(venue: Venue) {
        viewModel.navigateToVenueDetailsFragment(venue)
    }

    /**
     * Customer listener to handle text change and search submission
     */
    private val searchViewListener = object : SearchView.OnQueryTextListener {

        // TODO: Add debouncing timer

        override fun onQueryTextSubmit(query: String?): Boolean {
            return query?.let {
                viewModel.fetchNearbyVenues(it)
                true
            } ?: run {
                false
            }
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return if (newText.isNullOrBlank()) {
                viewModel.clearVenuesList()
                false
            } else {
                viewModel.fetchNearbyVenues(newText)
                true
            }
        }
    }

    /**
     * Adapter for the SearchFragment RecyclerView
     */
    class VenueAdapter(val adapterOnClick: (Venue) -> Unit) :
        RecyclerView.Adapter<VenueAdapter.ViewHolder>() {

        private var dataSet = listOf<Venue>()

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val venueListItemLayout: ConstraintLayout =
                view.findViewById(R.id.venueListItemContainer)
            val distanceTextView: TextView = view.findViewById(R.id.venueDistance)
            val nameTextView: TextView = view.findViewById(R.id.venueName)
            val categoryTextView: TextView = view.findViewById(R.id.venueCategory)
            val iconImageView: ImageView = view.findViewById(R.id.venueIcon)

            // TODO: Make a selector for favorite heart
            val favoriteImageView: ImageView = view.findViewById(R.id.favoriteIcon)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.venue_list_item, parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(dataSet[position]) {
                holder.venueListItemLayout.setOnClickListener {
                    adapterOnClick(this)
                }
                holder.nameTextView.text = this.name
                holder.distanceTextView.text = this.location.distance.toString()
                this.categories.firstOrNull()?.let {
                    holder.categoryTextView.text = it.name
                    // TODO: Resolve image loading issue
                    Glide.with(holder.iconImageView).load("${it.icon.prefix}${it.icon.suffix}")
                        .into(holder.iconImageView)
                }
                // TODO: Add selection/deselection onClick
            }
        }

        override fun getItemCount() = dataSet.size

        fun updateData(data: List<Venue>) {
            this.dataSet = data
            // Would implement DiffUtil instead in a real app
            notifyDataSetChanged()
        }
    }
}