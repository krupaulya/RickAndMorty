package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import com.example.rickandmorty.adapter.CharactersAdapter
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import com.example.rickandmorty.presentation.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment() {

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
        binding?.apply {
            characterRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            characterRecyclerView.adapter = charactersAdapter
            searchViewCharacters.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchViewCharacters.clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    return false
                }

            })
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        charactersAdapter.loadStateFlow.collect {
                            val state = it.refresh
                            characterProgressBar.isVisible = state is LoadState.Loading
                        }
                    }
                }
            }
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        charactersViewModel.getAllCharacters().collect {
                            charactersAdapter.submitData(it)
                        }
                    }
                }
            }
            charactersRefresh.setOnRefreshListener {
                charactersRefresh.isRefreshing = false
                viewLifecycleOwner.apply {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.CREATED) {
                            charactersViewModel.getAllCharacters().collect {
                                charactersAdapter.submitData(it)
                            }
                        }
                    }
                }
            }
            characterFrameLayout.visibility = View.GONE

            chFilerStatus.setOnClickListener {
                scrollViewGenderFilter.visibility = View.GONE
                statusLayout.visibility = View.VISIBLE
                if (characterFrameLayout.visibility == View.VISIBLE) {
                    characterFrameLayout.visibility = View.GONE
                } else {
                    characterFrameLayout.visibility = View.VISIBLE
                }
                statusAlive.setOnClickListener {
//                    charactersViewModel.getCharactersByStatus("alive")
                    charactersViewModel.characters.observe(viewLifecycleOwner) {

                    }
                }
            }
            chFilerGender.setOnClickListener {
                statusLayout.visibility = View.GONE
                scrollViewGenderFilter.visibility = View.VISIBLE
                if (characterFrameLayout.visibility == View.VISIBLE) {
                    characterFrameLayout.visibility = View.GONE
                } else {
                    characterFrameLayout.visibility = View.VISIBLE
                }
            }
        }
        charactersAdapter.setOnItemClickListener {
            val bundle = bundleOf("character" to it.id)
            findNavController().navigate(
                R.id.action_CharactersFragment_to_characterDetailsFragment,
                bundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}