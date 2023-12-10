package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.data.model.Locations
import com.example.rickandmorty.databinding.SharedRecyclerItemBinding
import javax.inject.Inject

class LocationsAdapter @Inject constructor() :
    RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {

    inner class LocationsViewHolder(val binding: SharedRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setItem(location: Locations.LocationsResults) {
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

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.setItem(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    private var onItemClickListener: ((Locations.LocationsResults) -> Unit)? = null

    fun setOnItemClickListener(listener: (Locations.LocationsResults) -> Unit) {
        onItemClickListener = listener
    }

    private val diffUtlCallBack = object : DiffUtil.ItemCallback<Locations.LocationsResults>() {
        override fun areItemsTheSame(oldItem: Locations.LocationsResults, newItem: Locations.LocationsResults): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Locations.LocationsResults,
            newItem: Locations.LocationsResults
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, diffUtlCallBack)

}

