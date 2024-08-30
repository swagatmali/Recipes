package com.swagatmali.search.domin.repository

import com.swagatmali.search.domin.model.Recipe
import com.swagatmali.search.domin.model.RecipeDetails

interface SearchRepository {

    suspend fun getRecipes(s: String): Result<List<Recipe>>

    suspend fun getRecipeDetails(i: String): Result<RecipeDetails>
}