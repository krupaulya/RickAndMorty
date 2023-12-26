package com.example.rickandmorty.ui.characters

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

    private var statusAliveFlag = false
    private var statusDeadFlag = false
    private var statusUnknownFlag = false
    private var maleGenderFlag = false
    private var femaleGenderFlag = false
    private var genderlessFlag = false
    private var unknownGenderFlag = false

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
        binding?.filterName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                charactersViewModel.updateNameFilterValue(p0.toString())
            }
        })
        binding?.filterSpecies?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                charactersViewModel.updateSpeciesFilterValue(p0.toString())
            }

        })
        binding?.filterType?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                charactersViewModel.updateTypeFilterValue(p0.toString())
            }

        })
        binding?.apply {
            characterRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            characterRecyclerView.adapter = charactersAdapter
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
                        charactersViewModel.getFilteredCharacters(
                            null,
                            null,
                            null,
                            null,
                            null
                        ).collect {
                            charactersAdapter.submitData(it)
                        }
                    }
                }
            }
            getCharacters()
            charactersRefresh.setOnRefreshListener {
                charactersRefresh.isRefreshing = false
                getCharacters()
            }
            scrollViewGenderFilter.visibility = View.GONE
            filterName.visibility = View.VISIBLE
            filterSpecies.visibility = View.GONE
            filterType.visibility = View.GONE
            statusLayout.visibility = View.GONE
            chFilterName.setOnClickListener {
                scrollViewGenderFilter.visibility = View.GONE
                filterName.visibility = View.VISIBLE
                filterSpecies.visibility = View.GONE
                filterType.visibility = View.GONE
                statusLayout.visibility = View.GONE
            }
            chFilterSpecies.setOnClickListener {
                scrollViewGenderFilter.visibility = View.GONE
                filterName.visibility = View.GONE
                filterSpecies.visibility = View.VISIBLE
                filterType.visibility = View.GONE
                statusLayout.visibility = View.GONE
            }
            chFilterType.setOnClickListener {
                scrollViewGenderFilter.visibility = View.GONE
                filterName.visibility = View.GONE
                filterSpecies.visibility = View.GONE
                filterType.visibility = View.VISIBLE
                statusLayout.visibility = View.GONE
            }
            chFilterStatus.setOnClickListener {
                scrollViewGenderFilter.visibility = View.GONE
                filterName.visibility = View.GONE
                filterSpecies.visibility = View.GONE
                filterType.visibility = View.GONE
                statusLayout.visibility = View.VISIBLE
            }
            statusAlive.setOnClickListener {
                statusAliveFlag = !statusAliveFlag
                statusDeadFlag = false
                statusUnknownFlag = false
                if (statusAliveFlag){
                    charactersViewModel.updateStatusFilterValue("Alive")
                }
                if (!statusAliveFlag){
                    charactersViewModel.updateStatusFilterValue(null)
                }
            }
            statusDead.setOnClickListener {
                statusDeadFlag = !statusDeadFlag
                statusAliveFlag = false
                statusUnknownFlag = false
                if (statusDeadFlag){
                    charactersViewModel.updateStatusFilterValue("Dead")
                }
                if (!statusDeadFlag){
                    charactersViewModel.updateStatusFilterValue(null)
                }
            }
            statusUnknown.setOnClickListener {
                statusUnknownFlag = !statusUnknownFlag
                statusAliveFlag = false
                statusDeadFlag = false
                if (statusUnknownFlag){
                    charactersViewModel.updateStatusFilterValue("unknown")
                }
                if (!statusUnknownFlag){
                    charactersViewModel.updateStatusFilterValue(null)
                }
            }
            chFilterGender.setOnClickListener {
                statusLayout.visibility = View.GONE
                filterName.visibility = View.GONE
                filterSpecies.visibility = View.GONE
                filterType.visibility = View.GONE
                scrollViewGenderFilter.visibility = View.VISIBLE
            }
            maleGender.setOnClickListener {
                maleGenderFlag = !maleGenderFlag
                femaleGenderFlag = false
                genderlessFlag = false
                unknownGenderFlag = false
                if (maleGenderFlag) {
                    charactersViewModel.updateGenderFilterValue("Male")
                }
                if (!maleGenderFlag) {
                    charactersViewModel.updateGenderFilterValue(null)
                }
            }
            femaleGender.setOnClickListener {
                maleGenderFlag = false
                femaleGenderFlag = !femaleGenderFlag
                genderlessFlag = false
                unknownGenderFlag = false
                if (femaleGenderFlag) {
                    charactersViewModel.updateGenderFilterValue("Female")
                }
                if (!femaleGenderFlag) {
                    charactersViewModel.updateGenderFilterValue(null)
                }
            }
            genderlessGender.setOnClickListener {
                maleGenderFlag = false
                femaleGenderFlag = false
                genderlessFlag = !genderlessFlag
                unknownGenderFlag = false
                if (genderlessFlag) {
                    charactersViewModel.updateGenderFilterValue("Genderless")
                }
                if (!genderlessFlag) {
                    charactersViewModel.updateGenderFilterValue(null)
                }
            }
            unknownGender.setOnClickListener {
                maleGenderFlag = false
                femaleGenderFlag = false
                genderlessFlag = false
                unknownGenderFlag = !unknownGenderFlag
                if (unknownGenderFlag) {
                    charactersViewModel.updateGenderFilterValue("unknown")
                }
                if (!unknownGenderFlag) {
                    charactersViewModel.updateGenderFilterValue(null)
                }
            }
        }
        charactersAdapter.setOnItemClickListener {
            val bundle = bundleOf("character" to it!!.id)
            findNavController().navigate(
                R.id.action_CharactersFragment_to_characterDetailsFragment,
                bundle
            )
        }
    }

    private fun getCharacters() {
        charactersViewModel.getCombinedLiveData()
            .observe(viewLifecycleOwner) { (name, status, species, type, gender) ->
                viewLifecycleOwner.apply {
                    lifecycleScope.launch {
                        charactersViewModel.getFilteredCharacters(
                            name,
                            status,
                            species,
                            type,
                            gender
                        ).collect {
                            charactersAdapter.submitData(it)
                        }
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}