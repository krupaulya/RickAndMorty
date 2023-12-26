package com.example.rickandmorty.ui.episodes

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
        binding?.filterNameEp?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                episodesViewModel.updateNameFilterValue(p0.toString())
            }

        })
        binding?.filterEpisodeEp?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                episodesViewModel.updateEpisodeFilterValue(p0.toString())
            }

        })
        episodesViewModel.getCombinedLiveData().observe(viewLifecycleOwner) { (name, episode) ->
            viewLifecycleOwner.lifecycleScope.launch {
                episodesViewModel.getEpisodes(name, episode).collect {
                    episodesAdapter.submitData(it)
                }
            }
        }
        binding?.apply {
            episodesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            episodesRecyclerView.adapter = episodesAdapter
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
            viewLifecycleOwner.apply {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.CREATED) {
                        episodesViewModel.getEpisodes(
                            null,
                            null
                        ).collect {
                            episodesAdapter.submitData(it)
                        }
                    }
                }
            }
            episodeBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            episodesRefresh.setOnRefreshListener {
                episodesRefresh.isRefreshing = false
                episodesViewModel.getCombinedLiveData().observe(viewLifecycleOwner) { (name, episode) ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        episodesViewModel.getEpisodes(name, episode).collect {
                            episodesAdapter.submitData(it)
                        }
                    }
                }
            }
            filterEpisodeEp.visibility = View.GONE
            filterNameEp.visibility = View.VISIBLE
            filterNameTextView.setOnClickListener {
                filterEpisodeEp.visibility = View.GONE
                filterNameEp.visibility = View.VISIBLE
            }
            filterEpisodeTextView.setOnClickListener {
                filterNameEp.visibility = View.GONE
                filterEpisodeEp.visibility = View.VISIBLE
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