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
import com.example.rickandmorty.adapter.CharactersAdapterForDetails
import com.example.rickandmorty.databinding.FragmentLocationDetailsBinding
import com.example.rickandmorty.presentation.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {

    private var binding: FragmentLocationDetailsBinding? = null
    private var locationId: Int? = 0
    private val locationsViewModel by viewModels<LocationsViewModel>()

    @Inject
    lateinit var charactersAdapterForDetails: CharactersAdapterForDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            locationId = it!!.getInt("locationId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationDetailsBinding.bind(view)
        charactersAdapterForDetails = CharactersAdapterForDetails()
        binding?.apply {
            locationsViewModel.getLocationById(locationId!!)
            locDetailBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            locationCharactersRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            locationCharactersRecyclerView.adapter = charactersAdapterForDetails
            refreshDetailsLocation.setOnRefreshListener {
                refreshDetailsLocation.isRefreshing = false
                getLocation()
            }
        }
        getLocation()
        locationsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding?.locationDetailsProgressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        charactersAdapterForDetails.setOnItemClickListener {
            val bundle = bundleOf("character" to it.id)
            findNavController().navigate(
                R.id.action_locationDetailsFragment_to_characterDetailsFragment,
                bundle
            )
        }
    }

    private fun getLocation() {
        locationsViewModel.location.observe(viewLifecycleOwner) {
            binding?.apply {
                locationDetailName.text = it.name
                locationDetailType.text = it.type
                locationDetailDimension.text = it.dimension
            }
            locationsViewModel.characters.observe(viewLifecycleOwner) {
                charactersAdapterForDetails.differ.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}