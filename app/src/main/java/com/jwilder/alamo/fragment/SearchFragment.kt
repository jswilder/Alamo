package com.jwilder.alamo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jwilder.alamo.R
import com.jwilder.alamo.VenueAdapter
import com.jwilder.alamo.databinding.FragmentSearchBinding
import com.jwilder.alamo.viewmodel.PlacesViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    val viewModel: PlacesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel.venueList.observe(viewLifecycleOwner, Observer { list ->
            binding.venuesRecyclerView.adapter = VenueAdapter(list)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnSearchClickListener {
            viewModel.fetchNearbyPlaces(binding.searchView.query.toString())
        }

        binding.mapsFAB.setOnClickListener {
            findNavController().navigate(R.id.action_SearchFragment_to_MapFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}