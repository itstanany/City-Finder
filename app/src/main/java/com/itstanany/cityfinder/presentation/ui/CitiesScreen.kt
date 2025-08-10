package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
      .fillMaxSize()
  ) {
    when (val state = uiState.value) {
      is CityScreenState.Error -> {
//      TODO()
      }

      CityScreenState.Idle -> {
        // render nothing for now
      }

      CityScreenState.Loading -> {
        CircularProgressIndicator(
          modifier = Modifier
            .align(androidx.compose.ui.Alignment.Center)
        )
      }

      is CityScreenState.Success -> {
        CityScreenContent(
          items = state.cities,
          onQueryChanged = {
            viewModel.handleUiEvent(CityScreenUiEvents.SearchQueryChanged(it))
          },
          searchQuery = state.query,
          totalCities = state.totalCities,
        )
      }
    }
  }
}
