package com.itstanany.cityfinder.presentation.stateHolder

import com.itstanany.cityfinder.presentation.model.CityGroup
import kotlinx.collections.immutable.ImmutableList

sealed class CityScreenState {
  data object Idle: CityScreenState()
  data object Loading : CityScreenState()
  data class Success(
    val cities: ImmutableList<CityGroup>,
    val query: String = "",
    val totalCities: Int,
    val isLoadingSearch: Boolean = false,
    val isRefreshing: Boolean = false
  ) : CityScreenState()
  data class Error(val message: String) : CityScreenState()
}

sealed class CityScreenUiEvents {
  data class SearchQueryChanged(val query: String) : CityScreenUiEvents()
  data object LoadData : CityScreenUiEvents()
  data object RefreshData : CityScreenUiEvents()
}
