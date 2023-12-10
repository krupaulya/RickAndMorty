package com.example.rickandmorty.ui.episodes

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
import com.example.rickandmorty.adapter.EpisodesAdapter
import com.example.rickandmorty.databinding.FragmentEpisodesBinding
import com.example.rickandmorty.presentation.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EpisodesFragment : Fragment() {

    private var binding: FragmentEpisodesBinding? = null

    private val episodesViewModel by viewModels<EpisodesViewModel>()

    @Inject
    lateinit var episodesAdapter: EpisodesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        binding!!.episodesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding!!.episodesRecyclerView.adapter = episodesAdapter
        episodesViewModel.getEpisodes()
        episodesViewModel.episodes.observe(viewLifecycleOwner) {
            episodesAdapter.differ.submitList(it)
        }
        episodesAdapter.setOnItemClickListener {
            val bundle = bundleOf("episodeId" to it.id)
            findNavController().navigate(R.id.action_EpisodesFragment_to_episodeDetailsFragment, bundle)
        }
        binding!!.episodeBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.apply {
            episodesRefresh.setOnRefreshListener {
                episodesRefresh.isRefreshing = false
                episodesViewModel.episodes.observe(viewLifecycleOwner) {
                    episodesAdapter.differ.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}