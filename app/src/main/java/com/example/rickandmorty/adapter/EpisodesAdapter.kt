package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.databinding.SharedRecyclerItemBinding
import javax.inject.Inject

class EpisodesAdapter @Inject constructor() :
    PagingDataAdapter<Episodes.EpisodesResults, EpisodesAdapter.EpisodesViewHolder>(diffUtilCallBack) {

    inner class EpisodesViewHolder(val binding: SharedRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setItem(episode: Episodes.EpisodesResults) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val binding =
            SharedRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.setItem(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    private var onItemClickListener: ((Episodes.EpisodesResults) -> Unit)? = null

    fun setOnItemClickListener(listener: (Episodes.EpisodesResults) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val diffUtilCallBack = object : DiffUtil.ItemCallback<Episodes.EpisodesResults>() {
            override fun areItemsTheSame(
                oldItem: Episodes.EpisodesResults,
                newItem: Episodes.EpisodesResults
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Episodes.EpisodesResults,
                newItem: Episodes.EpisodesResults
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}