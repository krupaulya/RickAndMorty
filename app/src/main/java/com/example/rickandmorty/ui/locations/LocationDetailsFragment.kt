package com.example.rickandmorty.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rickandmorty.databinding.FragmentLocationDetailsBinding
import com.example.rickandmorty.presentation.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {

    private var binding: FragmentLocationDetailsBinding? = null
    private var locationId: Int? = 0
    private val locationsViewModel by viewModels<LocationsViewModel>()

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
        binding?.apply {
            locationsViewModel.getLocationById(locationId!!)
            locationsViewModel.location.observe(viewLifecycleOwner) {
                locationDetailName.text = it.name
                locationDetailType.text = it.type
                locationDetailDimension.text = it.dimension
            }
            locDetailBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}