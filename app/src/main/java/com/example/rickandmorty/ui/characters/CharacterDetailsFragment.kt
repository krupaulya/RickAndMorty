package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmorty.presentation.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {

    private var binding: FragmentCharacterDetailsBinding? = null
    private val charactersViewModel by viewModels<CharacterViewModel>()
    private var characterId: Int? = null

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
        binding!!.chDetailBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        charactersViewModel.getCharacterById(characterId!!)
        charactersViewModel.character.observe(viewLifecycleOwner) {
            binding?.apply {
                Glide.with(view).load(it.image).into(characterDetailAvatar)
                characterDetailName.text = it.name
                characterDetailGender.text = it.gender
                characterDetailStatus.text = it.status
                characterDetailSpecies.text = it.species
                characterDetailLocation.text = it.location.url
                characterDetailEpisode.text = it.episode.toString()
                characterDetailOrigin.text = it.origin.url
                characterDetailType.text = it.type
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}