package com.danda.danda.ui.resepmasakanku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ItemResepMasakankuBinding

class ImageResepMasakankuAdapter(private val imageRecipeList: ArrayList<String>): RecyclerView.Adapter<ImageResepMasakankuAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemResepMasakankuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageRecipeList[position]

        holder.apply {
            binding.apply {
                itemView.apply {
                    Glide.with(itemView)
                        .load(image)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(photoFood)
                }
            }
        }
    }

    override fun getItemCount(): Int = imageRecipeList.size


    class ImageViewHolder (var binding: ItemResepMasakankuBinding) : RecyclerView.ViewHolder(binding.root)




//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(recipe: Recipe) {
//            with(binding) {
//
//                Glide.with(itemView.context)
//                    .load(recipe.photoUser)
//                    .placeholder(R.mipmap.ic_launcher_round)
//                    .error(R.mipmap.ic_launcher_round)
//                    .into(photoFood)
//            }
//        }
//    }
}