package com.danda.danda.model.dataclass

data class Recipe (
        var id: String? = null,
        val name: String?,
        val ingredients: String?,
        val tools: String?,
        val howToCook: String?
)