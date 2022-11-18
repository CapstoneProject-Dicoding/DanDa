package com.danda.danda.ui.resepmasakanku.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ItemResepMasakankuBinding
import com.danda.danda.model.dataclass.Recipe

class ResepMasakankuAdapter(private val recipeList: ArrayList<Recipe>): RecyclerView.Adapter<ResepMasakankuAdapter.ResepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val binding = ItemResepMasakankuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        holder.bind(recipeList[position])
    }

    override fun getItemCount(): Int = recipeList.size

    class ResepViewHolder (var binding: ItemResepMasakankuBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            with(binding) {
                tvNamaresep.text = recipe.nameRecipe

                Glide.with(itemView.context)
                    .load(recipe.photoUser)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(photoFood)
            }
        }
    }
}