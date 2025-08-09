package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itstanany.cityfinder.presentation.stateHolder.CityScreenState
import com.itstanany.cityfinder.presentation.stateHolder.CityScreenUiEvents
import com.itstanany.cityfinder.presentation.stateHolder.CityScreenViewModel

@Composable
fun CitiesScreen(
  viewModel: CityScreenViewModel,
  modifier: Modifier = Modifier
) {
  val uiState = viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.handleUiEvent(CityScreenUiEvents.LoadData)
  }
  Box(
    modifier = modifier
  ) {
    when (val state = uiState.value) {
      is CityScreenState.Error -> {
//      TODO()
      }

      CityScreenState.Idle -> {
//      TODO()
      }

      CityScreenState.Loading -> {
//      TODO()
      }

      is CityScreenState.Success -> {
        CityScreenContent(
          items = state.cities,
          onQueryChanged = {
            viewModel.handleUiEvent(CityScreenUiEvents.SearchQueryChanged(it))
          },
          searchQuery = state.query
        )
      }
    }
  }
}
