package com.danda.danda.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ItemListHomeBinding
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.detailrecipe.DetailRecipeActivity

@SuppressLint("NotifyDataSetChanged")
class HomeListAdapter : RecyclerView.Adapter<HomeListAdapter.HomeViewHolder>() {

    private var listRecipe: List<Recipe>? = null

    fun setListRecipe(list: List<Recipe>?){
        this.listRecipe = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val recipe = listRecipe?.get(position)

        holder.apply {
            binding.apply {
                itemView.apply {
                    nameRecipeTv.text = recipe?.nameRecipe
                    usernameTv.text = recipe?.username

                    Glide.with(itemView)
                        .load(recipe?.imgUrl)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(ivRecipe)

                    setOnClickListener {
                        val intent = Intent(context, DetailRecipeActivity::class.java)
                        intent.putExtra(DetailRecipeActivity.DATA_RECIPE, recipe)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when(listRecipe) {
            null -> 0
            else -> listRecipe!!.size
        }

    }

    inner class HomeViewHolder (var binding: ItemListHomeBinding) : RecyclerView.ViewHolder(binding.root)


}