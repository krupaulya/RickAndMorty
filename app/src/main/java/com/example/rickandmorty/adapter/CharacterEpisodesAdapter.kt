package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.databinding.SharedRecyclerItemBinding
import javax.inject.Inject

class CharacterEpisodesAdapter @Inject constructor() :
    RecyclerView.Adapter<CharacterEpisodesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: SharedRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: Episodes.EpisodesResults) {
            binding.apply {
                locEpName.text = episode.name
                locTypeEpDate.text = episode.airDate
                locDimEpEp.text = episode.episode
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(episode)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SharedRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    private var onItemClickListener: ((Episodes.EpisodesResults) -> Unit)? = null

    fun setOnItemClickListener(listener: (Episodes.EpisodesResults) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<Episodes.EpisodesResults>() {
        override fun areItemsTheSame(
            oldItem: Episodes.EpisodesResults, newItem: Episodes.EpisodesResults
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Episodes.EpisodesResults, newItem: Episodes.EpisodesResults
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}