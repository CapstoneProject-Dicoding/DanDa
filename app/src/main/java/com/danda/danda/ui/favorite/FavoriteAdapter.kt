package com.danda.danda.ui.favorite

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ListFavoriteBinding
import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.ui.editprofile.EditProfileActivity
import com.danda.danda.util.Constants
import com.danda.danda.util.showToast
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("NotifyDataSetChanged")
class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var databaseFirestore: FirebaseFirestore
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

                    btnDelete.setOnClickListener {
                        AlertDialog.Builder(context)
                            .setTitle("KONFIRMASI HAPUS")
                            .setMessage("Apakah anda yakin ingin menghapus resep ${favorite?.nameRecipe} ini?")
                            .setCancelable(false)

                            .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                                databaseFirestore = FirebaseFirestore.getInstance()

                                databaseFirestore.collection(Constants.FAVORITE).document(favorite!!.id)
                                    .delete()

                                    .addOnSuccessListener {
                                        context.showToast("Sukses hapus data ${favorite.nameRecipe}")
                                        (context as FavoriteActivity).recreate()
                                    }

                                    .addOnFailureListener {
                                        context.showToast("Gagal hapus data ${favorite.nameRecipe}")
                                        (context as FavoriteActivity).recreate()
                                    }
                                dialogInterface.dismiss()
                            }

                            .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                                dialogInterface.dismiss()
                            }
                            .show()


                    }
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