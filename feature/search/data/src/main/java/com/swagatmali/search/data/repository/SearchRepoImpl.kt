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
        return try {
            val response = searchApiService.getRecipes(s)
            if (response.isSuccessful) {
                response.body()?.meals?.let {
                    Result.success(it.toDomain())
                } ?: run {
                    Result.failure(Exception("Error occurred"))
                }
            } else {
                Result.failure(Exception("Error occurred"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipeDetails(i: String): Result<RecipeDetails> {
        return try {
            val response = searchApiService.getRecipeDetails(i)
            if (response.isSuccessful) {
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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}