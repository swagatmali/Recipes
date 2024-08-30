package com.swagatmali.search.data.remote

import com.swagatmali.search.data.model.RecipeDetailsResponse
import com.swagatmali.search.data.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {


    @GET("api/json/v1/1/search.php")
    suspend fun getRecipes(
        @Query("s") s: String
    ): Response<RecipeResponse>

    @GET("api/json/v1/1/lookup.php")
    suspend fun getRecipeDetails(
        @Query("i") i: String
    ): Response<RecipeDetailsResponse>
}