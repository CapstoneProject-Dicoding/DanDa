package com.danda.danda.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ListFavoriteBinding
import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.ui.detailrecipe.DetailRecipeActivity

@SuppressLint("NotifyDataSetChanged")
class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var listFavorite: List<Favorite>? = null

    fun setListFavorite(list: List<Favorite>) {
        this.listFavorite = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ListFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = listFavorite?.get(position)

        holder.apply {
            binding.apply {
                itemView.apply {
                    Glide.with(itemView.context)
                        .load(favorite?.imgUrl)
                        .error(R.drawable.ic_food)
                        .into(photoRecipe)

                    nameRecipeTv.text = favorite?.nameRecipe
                    usernameTv.text = favorite?.username

//                    setOnClickListener {
//                        val intent = Intent(context, DetailRecipeActivity::class.java)
//                        intent.putExtra(DetailRecipeActivity.DATA_RECIPE, favorite)
//                        context.startActivity(intent)
//                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when (listFavorite) {
            null -> 0
            else -> listFavorite!!.size
        }
    }


    inner class FavoriteViewHolder(val binding: ListFavoriteBinding): RecyclerView.ViewHolder(binding.root)
}