package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.data.model.LocationsResults
import com.example.rickandmorty.databinding.SharedRecyclerItemBinding
import javax.inject.Inject

class LocationsAdapter @Inject constructor() :
    PagingDataAdapter<LocationsResults, LocationsAdapter.LocationsViewHolder>(
        diffUtlCallBack
    ) {

    inner class LocationsViewHolder(val binding: SharedRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setItem(location: LocationsResults) {
            binding.apply {
                locEpName.text = location.name
                locTypeEpDate.text = location.type
                locDimEpEp.text = location.dimension
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(location)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val binding =
            SharedRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.setItem(getItem(position)!!)
    }

    private var onItemClickListener: ((LocationsResults) -> Unit)? = null

    fun setOnItemClickListener(listener: (LocationsResults) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val diffUtlCallBack = object : DiffUtil.ItemCallback<LocationsResults>() {
            override fun areItemsTheSame(
                oldItem: LocationsResults,
                newItem: LocationsResults
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: LocationsResults,
                newItem: LocationsResults
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

