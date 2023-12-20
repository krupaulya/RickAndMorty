package com.example.rickandmorty.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.LocationsAdapter
import com.example.rickandmorty.databinding.FragmentLocationsBinding
import com.example.rickandmorty.presentation.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        binding?.apply {
            locationRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            locationRecyclerView.adapter = locationsAdapter
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        locationsViewModel.locationList.collect {
                            locationsAdapter.submitData(it)
                        }
                    }
                }
            }
            locationBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        locationsAdapter.loadStateFlow.collect {
                            val state = it.refresh
                            locationProgressBar.isVisible = state is LoadState.Loading
                        }
                    }
                }
            }
            locationsRefresh.setOnRefreshListener {
                locationsRefresh.isRefreshing = false
                viewLifecycleOwner.apply {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.CREATED) {
                            locationsViewModel.locationList.collect {
                                locationsAdapter.submitData(it)
                            }
                        }
                    }
                }
            }
        }
        locationsAdapter.setOnItemClickListener {
            val bundle = bundleOf("locationId" to it.id)
            findNavController().navigate(
                R.id.action_LocationsFragment_to_locationDetailsFragment,
                bundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}