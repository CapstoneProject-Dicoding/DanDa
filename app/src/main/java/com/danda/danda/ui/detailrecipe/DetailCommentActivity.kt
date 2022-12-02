package com.danda.danda.ui.detailrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.danda.danda.databinding.ActivityDetailCommentBinding
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCommentBinding
    private val commentListAdapter: DetailCommentAdapter by lazy(::DetailCommentAdapter)
    private val detailViewModel by viewModels<DetailRecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeData = intent.getParcelableExtra<Recipe>(DATA_NAME_RECIPE) as Recipe

        getListComment(recipeData.nameRecipe)
    }

    private fun getListComment(nameRecipe: String) {
        binding.rvDetailComment.apply {
            layoutManager = LinearLayoutManager(this@DetailCommentActivity)
            adapter = commentListAdapter
            setHasFixedSize(true)
        }

        detailViewModel.listComment.observe(this) { status ->
            when (status) {
                is Result.Success -> {
                    commentListAdapter.setListComment(status.data)
                }
                else -> {}
            }
        }

        detailViewModel.getCommentListDetail(nameRecipe)
    }

    companion object {
        const val DATA_NAME_RECIPE = "detail_name_recipe"
    }
}