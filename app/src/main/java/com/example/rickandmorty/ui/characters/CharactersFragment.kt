package com.example.rickandmorty.ui.characters

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
import com.example.rickandmorty.adapter.CharactersAdapter
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import com.example.rickandmorty.presentation.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(){

    private var binding: FragmentCharactersBinding? = null

    private val charactersViewModel by viewModels<CharacterViewModel>()

    @Inject
    lateinit var charactersAdapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharactersBinding.bind(view)
        charactersAdapter = CharactersAdapter()
        binding!!.characterRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding!!.characterRecyclerView.adapter = charactersAdapter
        charactersViewModel.getCharacters()
        charactersViewModel.characters.observe(viewLifecycleOwner) {
            charactersAdapter.differ.submitList(it)
        }
        charactersAdapter.setOnItemClickListener {
            val bundle = bundleOf("character" to it.id)
            findNavController().navigate(R.id.action_CharactersFragment_to_characterDetailsFragment, bundle)
        }
        binding?.apply {
            charactersRefresh.setOnRefreshListener {
                charactersRefresh.isRefreshing = false
                charactersViewModel.characters.observe(viewLifecycleOwner) {
                    charactersAdapter.differ.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}