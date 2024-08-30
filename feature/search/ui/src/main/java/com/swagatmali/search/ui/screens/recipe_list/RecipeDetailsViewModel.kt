package com.swagatmali.search.ui.screens.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swagatmali.common.utils.NetworkResult
import com.swagatmali.common.utils.UiText
import com.swagatmali.search.domin.use_cases.GetRecipeDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetails.UiState())
    val uiState: StateFlow<RecipeDetails.UiState> = _uiState.asStateFlow()


    fun onEvent(event: RecipeDetails.Event) {
        when (event) {
            is RecipeDetails.Event.FetchRecipeDetails -> recipeDetails(event.id)
        }
    }

    private fun recipeDetails(id: String) = getRecipeDetailsUseCase.invoke(id)
        .onEach { result ->
            when (result) {
                is NetworkResult.Error -> _uiState.update {
                    RecipeDetails.UiState(error = UiText.RemoteString(result.message.toString()))
                }

                is NetworkResult.Loading -> _uiState.update {
                    RecipeDetails.UiState(isLoading = true)
                }

                is NetworkResult.Success -> _uiState.update {
                    RecipeDetails.UiState(data = result.data)
                }
            }
        }.launchIn(viewModelScope)

}


object RecipeDetails {
    data class UiState(
        val isLoading: Boolean = false,
        val data: com.swagatmali.search.domin.model.RecipeDetails? = null,
        val error: UiText = UiText.Idle
    )

    sealed interface Navigation {
    }

    sealed interface Event {
        data class FetchRecipeDetails(val id: String) : Event
    }
}