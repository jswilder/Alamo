package com.jwilder.alamo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jwilder.alamo.R
import com.jwilder.alamo.VenueAdapter
import com.jwilder.alamo.databinding.FragmentSearchBinding
import com.jwilder.alamo.viewmodel.VenuesSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    val viewModel: VenuesSharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.venuesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VenueAdapter()
        }

        viewModel.venueList.observe(viewLifecycleOwner, { list ->
            (binding.venuesRecyclerView.adapter as VenueAdapter).updateData(list)
        })

        viewModel.showMapFAB.observe(viewLifecycleOwner, {
            binding.mapsFAB.isVisible = it
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
}