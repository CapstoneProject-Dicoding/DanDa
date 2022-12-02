package com.danda.danda.ui.detailrecipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ListCommentBinding
import com.danda.danda.model.dataclass.Comment

@SuppressLint("NotifyDataSetChanged")
class DetailCommentAdapter: RecyclerView.Adapter<DetailCommentAdapter.DetailCommentViewHolder>() {

    private var listComment: List<Comment>? = null

    fun setListComment(list: List<Comment>){
        this.listComment = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailCommentViewHolder {
        val binding = ListCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailCommentViewHolder, position: Int) {
        val recipe = listComment?.get(position)

        holder.apply {
            binding.apply {
                itemView.apply {
                    usernameTv.text = recipe?.emailUser
                    commentValue.text = recipe?.comment

                    Glide.with(itemView)
                        .load(recipe?.imgUrl)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(photoUser)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when(listComment) {
            null -> 0
            else -> listComment!!.size
        }

    }

    inner class DetailCommentViewHolder (var binding: ListCommentBinding) : RecyclerView.ViewHolder(binding.root)
}