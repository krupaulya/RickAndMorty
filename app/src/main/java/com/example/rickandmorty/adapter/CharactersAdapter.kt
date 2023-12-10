package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.data.model.Characters
import com.example.rickandmorty.databinding.CharacterRecyclerItemBinding
import javax.inject.Inject

class CharactersAdapter @Inject constructor(

) :
    RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    inner class CharactersViewHolder(
        val binding: CharacterRecyclerItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setItem(character: Characters.CharactersResults) {
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

    private var onItemClickListener: ((Characters.CharactersResults) -> Unit)? = null

    fun setOnItemClickListener(listener: (Characters.CharactersResults) -> Unit) {
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding =
            CharacterRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.setItem(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Characters.CharactersResults>() {
        override fun areItemsTheSame(
            oldItem: Characters.CharactersResults,
            newItem: Characters.CharactersResults
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Characters.CharactersResults,
            newItem: Characters.CharactersResults
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}