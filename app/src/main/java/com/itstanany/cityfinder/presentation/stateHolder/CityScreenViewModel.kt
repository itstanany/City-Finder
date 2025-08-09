package com.itstanany.cityfinder.presentation.stateHolder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itstanany.cityfinder.domain.usecase.GetAllCitiesUseCase
import com.itstanany.cityfinder.domain.usecase.SearchCitiesByPrefixUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CityScreenViewModel @Inject constructor(
  private val getAllCitiesUseCase: GetAllCitiesUseCase,
  private val searchCitiesByPrefixUseCase: SearchCitiesByPrefixUseCase
): ViewModel() {
  private val _uiState = MutableStateFlow<CityScreenState>(CityScreenState.Idle)
  val uiState = _uiState.asStateFlow()
  fun handleUiEvent(events: CityScreenUiEvents) {
    when (events) {
      is CityScreenUiEvents.LoadData -> {
        viewModelScope.launch {
          val result = getAllCitiesUseCase()
          _uiState.value = CityScreenState.Success(result.toImmutableList())
        }
      }
      is CityScreenUiEvents.SearchQueryChanged -> {
        _uiState.update {
          if (it is CityScreenState.Success) {
            it.copy(query = events.query)
          } else {
            it
          }
        }
        viewModelScope.launch {
          val result = searchCitiesByPrefixUseCase(events.query)
          if (_uiState.value is CityScreenState.Success) {
            _uiState.update {
              (it as CityScreenState.Success).copy(cities = result.toImmutableList())
            }
          }

        }
      }
    }

  }
}
