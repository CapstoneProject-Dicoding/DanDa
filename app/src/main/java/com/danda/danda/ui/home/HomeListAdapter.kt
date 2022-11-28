package com.danda.danda.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ItemListHomeBinding
import com.danda.danda.databinding.ItemResepMasakankuBinding
import com.danda.danda.model.dataclass.Recipe
import com.denzcoskun.imageslider.constants.ScaleTypes

class HomeListAdapter : RecyclerView.Adapter<HomeListAdapter.HomeViewHolder>() {

    private var listRecipe = mutableListOf<Recipe>()

    fun setListRecipe(list: MutableList<Recipe>){
        this.listRecipe = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val recipe = listRecipe[position]

        holder.apply {
            binding.apply {
                itemView.apply {
                    tvNameRecipe.text = recipe.nameRecipe

                    Glide.with(itemView)
                        .load(recipe.imgUrl)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(ivRecipe)
                }
            }
        }
    }

    override fun getItemCount(): Int = listRecipe.size

    inner class HomeViewHolder (var binding: ItemListHomeBinding) : RecyclerView.ViewHolder(binding.root)


}