package com.example.rickandmorty.ui.episodes

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
import com.example.rickandmorty.adapter.EpisodesAdapter
import com.example.rickandmorty.databinding.FragmentEpisodesBinding
import com.example.rickandmorty.presentation.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EpisodesFragment : Fragment() {

    private var binding: FragmentEpisodesBinding? = null

    private val episodesViewModel by viewModels<EpisodesViewModel>()

    @Inject
    lateinit var episodesAdapter: EpisodesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEpisodesBinding.bind(view)
        episodesAdapter = EpisodesAdapter()
        binding?.apply {
            episodesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            episodesRecyclerView.adapter = episodesAdapter
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        episodesViewModel.getEpisodes().collect {
                            episodesAdapter.submitData(it)
                        }
                    }
                }
            }
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        episodesAdapter.loadStateFlow.collect {
                            val state = it.refresh
                            episodeProgressBar.isVisible = state is LoadState.Loading
                        }
                    }
                }
            }
            episodeBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            episodesRefresh.setOnRefreshListener {
                episodesRefresh.isRefreshing = false
                viewLifecycleOwner.apply {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.CREATED) {
                            episodesViewModel.getEpisodes().collect {
                                episodesAdapter.submitData(it)
                            }
                        }
                    }
                }
            }
        }
        episodesAdapter.setOnItemClickListener {
            val bundle = bundleOf("episodeId" to it.id)
            findNavController().navigate(
                R.id.action_EpisodesFragment_to_episodeDetailsFragment,
                bundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}