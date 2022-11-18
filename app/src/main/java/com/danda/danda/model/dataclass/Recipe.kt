package com.danda.danda.model.dataclass

data class Recipe (
        var id: String? = null,
        val nameRecipe: String? = null,
        val ingredients: String? = null,
        val tools: String? = null,
        val howToCook: String? = null,
        val photoUser: String? = null
)