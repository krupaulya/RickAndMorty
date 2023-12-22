package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.databinding.CharacterRecyclerItemBinding
import javax.inject.Inject

class CharactersAdapterForDetails @Inject constructor() :
    RecyclerView.Adapter<CharactersAdapterForDetails.ViewHolder>() {

    inner class ViewHolder(val binding: CharacterRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharactersResults) {
            binding.apply {
                setAvatar(characterAvatar, character.image)
                characterGender.text = character.gender
                characterName.text = character.name
                characterStatus.text = character.status
                characterSpecies.text = character.species
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(character)
                    }
                }
            }
        }

        private fun setAvatar(imageView: ImageView, url: String?) {
            url?.let {
                Glide.with(imageView).load(it).into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CharacterRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    private var onItemClickListener: ((CharactersResults) -> Unit)? = null

    fun setOnItemClickListener(listener: (CharactersResults) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<CharactersResults>() {
        override fun areItemsTheSame(
            oldItem: CharactersResults, newItem: CharactersResults
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CharactersResults, newItem: CharactersResults
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}