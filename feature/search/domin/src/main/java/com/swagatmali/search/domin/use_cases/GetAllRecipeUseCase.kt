package com.swagatmali.search.domin.use_cases

import com.swagatmali.common.utils.NetworkResult
import com.swagatmali.search.domin.model.Recipe
import com.swagatmali.search.domin.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllRecipeUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(s: String) = flow<NetworkResult<List<Recipe>>> {

        emit(NetworkResult.Loading())

        val response = searchRepository.getRecipes(s)
        if (response.isSuccess) {
            emit(NetworkResult.Success(data = response.getOrThrow()))
        } else {
            emit(NetworkResult.Error(message = response.exceptionOrNull()?.localizedMessage))
        }
    }.catch {
        emit(NetworkResult.Error(message = it.message))
    }.flowOn(Dispatchers.IO)
}
