package com.danda.danda.model.dataclass

data class Recipe (
        var name: String? = null,
        var ingredients: String? = null,
        var tools: String? = null,
        var howToCook: String? = null,
        var comment: String? = null
)