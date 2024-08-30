package com.swagatmali.search.domin.use_cases

import com.swagatmali.common.utils.NetworkResult
import com.swagatmali.search.domin.model.RecipeDetails
import com.swagatmali.search.domin.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    suspend operator fun invoke(i: String) = flow<NetworkResult<RecipeDetails>> {
        emit(NetworkResult.Loading())
        val response = searchRepository.getRecipeDetails(i)
        if (response.isSuccess) {
            emit(NetworkResult.Success(data = response.getOrThrow()))
        } else {
            emit(NetworkResult.Error(message = response.exceptionOrNull()?.localizedMessage))
        }
    }.catch {
        emit(NetworkResult.Error(message = it.message))
    }.flowOn(Dispatchers.IO)
}