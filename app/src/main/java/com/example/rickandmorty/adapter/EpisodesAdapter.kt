package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.data.model.EpisodesResults
import com.example.rickandmorty.databinding.SharedRecyclerItemBinding
import javax.inject.Inject

class EpisodesAdapter @Inject constructor() :
    PagingDataAdapter<EpisodesResults, EpisodesAdapter.EpisodesViewHolder>(diffUtilCallBack) {

    inner class EpisodesViewHolder(val binding: SharedRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setItem(episode: EpisodesResults) {
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
    }

    private var onItemClickListener: ((EpisodesResults) -> Unit)? = null

    fun setOnItemClickListener(listener: (EpisodesResults) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val diffUtilCallBack =
            object : DiffUtil.ItemCallback<EpisodesResults>() {
            override fun areItemsTheSame(
                oldItem: EpisodesResults,
                newItem: EpisodesResults
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: EpisodesResults,
                newItem: EpisodesResults
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}