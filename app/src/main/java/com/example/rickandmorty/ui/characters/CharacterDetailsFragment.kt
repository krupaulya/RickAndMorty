package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.CharacterEpisodesAdapter
import com.example.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmorty.presentation.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {

    private var binding: FragmentCharacterDetailsBinding? = null
    private val charactersViewModel by viewModels<CharacterViewModel>()
    private var characterId: Int? = null

    @Inject
    lateinit var episodesAdapter: CharacterEpisodesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            characterId = it!!.getInt("character")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharacterDetailsBinding.bind(view)
        episodesAdapter = CharacterEpisodesAdapter()
        binding?.apply {
            chDetailBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            characterEpisodesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            characterEpisodesRecyclerView.adapter = episodesAdapter
            characterDetailOrigin.setOnClickListener {
                charactersViewModel.originID.observe(viewLifecycleOwner) {
                    val bundle = bundleOf("locationId" to it)
                    findNavController().navigate(
                        R.id.action_characterDetailsFragment_to_locationDetailsFragment,
                        bundle
                    )
                }
            }
            characterDetailLocation.setOnClickListener {
                charactersViewModel.locationID.observe(viewLifecycleOwner) {
                    val bundle = bundleOf("locationId" to it)
                    findNavController().navigate(
                        R.id.action_characterDetailsFragment_to_locationDetailsFragment,
                        bundle
                    )
                }
            }
        }
        episodesAdapter.setOnItemClickListener {
            val bundle = bundleOf("episodeId" to it.id)
            findNavController().navigate(
                R.id.action_characterDetailsFragment_to_episodeDetailsFragment,
                bundle
            )
        }
        charactersViewModel.getCharacterById(characterId!!)
        charactersViewModel.character.observe(viewLifecycleOwner) {
            binding?.apply {
                Glide.with(view).load(it.image).into(characterDetailAvatar)
                characterDetailName.text = it.name
                characterDetailGender.text = it.gender
                characterDetailStatus.text = it.status
                characterDetailSpecies.text = it.species
                characterDetailLocation.text = it.location.name
                characterDetailOrigin.text = it.origin.name
                characterDetailType.text = it.type
            }
        }
        charactersViewModel.episodes.observe(viewLifecycleOwner) {
            episodesAdapter.differ.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}