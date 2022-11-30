package com.danda.danda.ui.resepmasakanku

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ItemResepMasakankuBinding
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.detailrecipe.DetailRecipeActivity

@SuppressLint("NotifyDataSetChanged")
class ResepMasakankuAdapter: RecyclerView.Adapter<ResepMasakankuAdapter.ResepViewHolder>() {

    private var listRecipe: List<Recipe>? = null

    fun setListRecipe(list: List<Recipe>){
        this.listRecipe = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val binding = ItemResepMasakankuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val recipe = listRecipe?.get(position)

        holder.apply {
            binding.apply {
                itemView.apply {
                    tvNamaresep.text = recipe?.nameRecipe

                    Glide.with(itemView)
                        .load(recipe?.imgUrl)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(photoFood)

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

    inner class ResepViewHolder (var binding: ItemResepMasakankuBinding) : RecyclerView.ViewHolder(binding.root)
}