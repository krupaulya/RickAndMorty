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
import com.example.rickandmorty.adapter.CharactersAdapterForDetails
import com.example.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.example.rickandmorty.presentation.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeDetailsFragment : Fragment() {

    private var binding: FragmentEpisodeDetailsBinding? = null
    private var episodeId: Int? = 0
    private val episodesViewModel by viewModels<EpisodesViewModel>()

    @Inject
    lateinit var charactersAdapterForDetails: CharactersAdapterForDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            episodeId = it!!.getInt("episodeId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEpisodeDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEpisodeDetailsBinding.bind(view)
        charactersAdapterForDetails = CharactersAdapterForDetails()
        binding?.apply {
            episodesViewModel.getEpisodeById(episodeId!!)
            epDetailBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            episodeCharactersRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            episodeCharactersRecyclerView.adapter = charactersAdapterForDetails
            refreshDetailsEpisodes.setOnRefreshListener {
                refreshDetailsEpisodes.isRefreshing = false
                getEpisode(view)
            }
        }
        getEpisode(view)
        episodesViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding?.episodeDetailsProgressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        charactersAdapterForDetails.setOnItemClickListener {
            val bundle = bundleOf("character" to it.id)
            findNavController().navigate(
                R.id.action_episodeDetailsFragment_to_characterDetailsFragment,
                bundle
            )
        }
    }

    private fun getEpisode(view: View) {
        episodesViewModel.episode.observe(viewLifecycleOwner) {
            binding?.apply {
                episodeDetailName.text = it.name
                episodeDetailDate.text = it.airDate
                episodeDetailEpisode.text = it.episode
            }
        }
        episodesViewModel.characters.observe(viewLifecycleOwner) {
            charactersAdapterForDetails.differ.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}