package com.swagatmali.search.ui.screens.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swagatmali.common.utils.NetworkResult
import com.swagatmali.common.utils.UiText
import com.swagatmali.search.domin.model.Recipe
import com.swagatmali.search.domin.use_cases.GetAllRecipeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
    private val getAllRecipeUseCase: GetAllRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeList.UiState())
    val uiState: StateFlow<RecipeList.UiState> = _uiState.asStateFlow()

    fun onEvent(event : RecipeList.Event) {
        when(event) {
            is RecipeList.Event.SearchRecipe -> searchRecipe(event.q)
        }
    }

    private fun searchRecipe(s: String) = getAllRecipeUseCase.invoke(s)
        .onEach { result ->
            when (result) {
                is NetworkResult.Loading -> _uiState.update {
                    RecipeList.UiState(isLoading = true)
                }

                is NetworkResult.Error -> _uiState.update {
                    RecipeList.UiState(error = UiText.RemoteString(result.message.toString()))
                }

                is NetworkResult.Success -> _uiState.update {
                    RecipeList.UiState(data = result.data)
                }
            }
        }.launchIn(viewModelScope)
}

object RecipeList {
    data class UiState(
        val isLoading: Boolean = false,
        val data: List<Recipe>? = null,
        val error: UiText? = UiText.Idle
    )

    sealed interface Navigation {
        data class GotoRecipeDetails(val id: String) : Navigation
    }

    sealed interface Event {
        data class SearchRecipe(val q: String) : Event
    }
}