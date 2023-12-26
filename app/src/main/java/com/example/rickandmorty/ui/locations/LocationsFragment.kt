package com.example.rickandmorty.ui.locations

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        binding?.filterNameLoc?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                locationsViewModel.updateNameFilterValue(p0.toString())
            }

        })
        binding?.filterTypeLoc?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                locationsViewModel.updateTypeFilterValue(p0.toString())
            }

        })
        binding?.filterDimensionLoc?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                locationsViewModel.updateDimensionFilterValue(p0.toString())
            }

        })
        getLocations()
        binding?.apply {
            locationRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            locationRecyclerView.adapter = locationsAdapter
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        locationsViewModel.getLocations(null, null, null).collect {
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
                getLocations()
            }
            filterNameLoc.visibility = View.VISIBLE
            filterTypeLoc.visibility = View.GONE
            filterDimensionLoc.visibility = View.GONE
            epFilterName.setOnClickListener {
                filterNameLoc.visibility = View.VISIBLE
                filterTypeLoc.visibility = View.GONE
                filterDimensionLoc.visibility = View.GONE
            }
            epFilterType.setOnClickListener {
                filterNameLoc.visibility = View.GONE
                filterTypeLoc.visibility = View.VISIBLE
                filterDimensionLoc.visibility = View.GONE
            }
            epFilterDimension.setOnClickListener {
                filterNameLoc.visibility = View.GONE
                filterTypeLoc.visibility = View.GONE
                filterDimensionLoc.visibility = View.VISIBLE
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

    private fun getLocations() {
        locationsViewModel.getCombinedLiveData()
            .observe(viewLifecycleOwner) { (name, type, dimension) ->
                viewLifecycleOwner.lifecycleScope.launch {
                    locationsViewModel.getLocations(
                        name,
                        type,
                        dimension
                    ).collect {
                        locationsAdapter.submitData(it)
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}