package com.itstanany.cityfinder.presentation.stateHolder

import com.itstanany.cityfinder.presentation.model.CityGroup
import kotlinx.collections.immutable.ImmutableList

/**
 * Represents the different states of the City screen.
 * This sealed class is used to manage the UI state based on data loading, success, or error.
 */
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

/**
 * Represents the different UI events that can occur on the City screen.
 * This sealed class is used to handle user interactions and data loading requests.
 */
sealed class CityScreenUiEvents {
  data class SearchQueryChanged(val query: String) : CityScreenUiEvents()
  data object LoadData : CityScreenUiEvents()
  data object RefreshData : CityScreenUiEvents()
}
