package com.itstanany.cityfinder.presentation.stateHolder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itstanany.cityfinder.domain.usecase.GetAllCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CityScreenViewModel @Inject constructor(
  private val getAllCitiesUseCase: GetAllCitiesUseCase,
): ViewModel() {
  private val _uiState = MutableStateFlow<CityScreenState>(CityScreenState.Idle)
  val uiState = _uiState.asStateFlow()
  fun handleUiEvent(events: CityScreenUiEvents) {
    when (events) {
      is CityScreenUiEvents.LoadData -> {
        viewModelScope.launch(Dispatchers.Default) {
          val result = getAllCitiesUseCase()
          _uiState.value = CityScreenState.Success(result.toImmutableList())
        }
      }
      is CityScreenUiEvents.SearchQueryChanged -> {
        // todo later
      }
    }

  }
}
