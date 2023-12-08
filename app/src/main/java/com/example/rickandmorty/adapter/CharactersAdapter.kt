package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.data.model.Results
import com.example.rickandmorty.databinding.CharacterRecyclerItemBinding
import javax.inject.Inject

class CharactersAdapter @Inject constructor() : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    var characters = mutableListOf<Results>()

    class CharactersViewHolder(val binding: CharacterRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding =
            CharacterRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersViewHolder(binding)
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = characters[position]
        setAvatar(holder.binding.characterAvatar, character.image)
        holder.binding.characterGender.text = character.gender
        holder.binding.characterName.text = character.name
        holder.binding.characterStatus.text = character.status
        holder.binding.characterSpecies.text = character.species
    }

    fun setData(characters: List<Results>) {
        this.characters = characters.toMutableList()
        notifyDataSetChanged()
    }

    private fun setAvatar(imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(imageView).load(it).into(imageView)
        }
    }
}