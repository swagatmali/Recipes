package com.swagatmali.search.data.repository

import com.swagatmali.search.data.mappers.toDomain
import com.swagatmali.search.data.remote.SearchApiService
import com.swagatmali.search.domin.model.Recipe
import com.swagatmali.search.domin.model.RecipeDetails
import com.swagatmali.search.domin.repository.SearchRepository

class SearchRepoImpl(
    private val searchApiService: SearchApiService
) : SearchRepository {

    override suspend fun getRecipes(s: String): Result<List<Recipe>> {

        val response = searchApiService.getRecipes(s)
        return if (response.isSuccessful) {
            response.body()?.meals?.let {
                Result.success(it.toDomain())
            } ?: run {
                Result.failure(Exception("Error occurred"))
            }
        } else {
            Result.failure(Exception("Error occurred"))
        }
    }

    override suspend fun getRecipeDetails(i: String): Result<RecipeDetails> {
        val response = searchApiService.getRecipeDetails(i)
        return if (response.isSuccessful) {
            response.body()?.meals?.let {
                if (it.isNotEmpty()) {
                    Result.success(it.first().toDomain())
                } else {
                    Result.failure(Exception("Error occurred"))
                }
            } ?: run {
                Result.failure(Exception("Error occurred"))
            }
        } else {
            Result.failure(Exception("Error occurred"))
        }
    }
}