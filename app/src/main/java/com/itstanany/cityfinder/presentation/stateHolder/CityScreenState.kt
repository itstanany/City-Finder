package com.itstanany.cityfinder.presentation.stateHolder

import com.itstanany.cityfinder.domain.model.City
import kotlinx.collections.immutable.ImmutableList

sealed class CityScreenState {
  data object Idle: CityScreenState()
  data object Loading : CityScreenState()
  data class Success(
    val cities: ImmutableList<City>,
    val query: String = ""
  ) : CityScreenState()
  data class Error(val message: String) : CityScreenState()
}

sealed class CityScreenUiEvents {
  data class SearchQueryChanged(val query: String) : CityScreenUiEvents()
  data object LoadData : CityScreenUiEvents()
}
