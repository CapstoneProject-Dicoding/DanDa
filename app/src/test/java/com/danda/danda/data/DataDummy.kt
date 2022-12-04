package com.danda.danda.data

import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Result

object DataDummy {
    fun showDataDummyRecipeResponse(): Recipe {
        return Recipe(
            "dsa13",
            "name",
            "ingredients",
            "tools",
            "how to cook",
            "url",
            "email"
        )
    }


    fun showDataDummyCommentResponse(): Result<List<Comment>> {
        val listComment = ArrayList<Comment>()
        for (i in 0 until 5) {
            val comment = Comment(
                "dsa13",
                "name",
                "very good",
                "url",
                "email",
            )
            listComment.add(comment)
        }

        return Result.Success(listComment)
    }
}