package com.example.rickandmorty.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.LocationsAdapter
import com.example.rickandmorty.databinding.FragmentLocationsBinding
import com.example.rickandmorty.presentation.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    private var binding: FragmentLocationsBinding? = null

    private val locationsViewModel by viewModels<LocationsViewModel>()

    @Inject
    lateinit var locationsAdapter: LocationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationsBinding.bind(view)
        locationsAdapter = LocationsAdapter()
        binding!!.locationRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding!!.locationRecyclerView.adapter = locationsAdapter
        locationsViewModel.getLocations()
        locationsViewModel.locations.observe(viewLifecycleOwner) {
            locationsAdapter.differ.submitList(it)
        }
        locationsAdapter.setOnItemClickListener {
            val bundle = bundleOf("locationId" to it.id)
            findNavController().navigate(R.id.action_LocationsFragment_to_locationDetailsFragment, bundle)
        }
        binding!!.locationBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.apply {
            locationsRefresh.setOnRefreshListener {
                locationsRefresh.isRefreshing = false
                locationsViewModel.locations.observe(viewLifecycleOwner) {
                    locationsAdapter.differ.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}