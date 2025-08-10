package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.itstanany.cityfinder.R
import com.itstanany.cityfinder.presentation.stateHolder.CityScreenState
import com.itstanany.cityfinder.presentation.stateHolder.CityScreenUiEvents
import com.itstanany.cityfinder.presentation.stateHolder.CityScreenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CitiesScreen(
  viewModel: CityScreenViewModel,
  modifier: Modifier = Modifier
) {
  val uiState = viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.handleUiEvent(CityScreenUiEvents.LoadData)
  }
  val pullRefreshState =
    rememberPullRefreshState(
      refreshing = false,
      onRefresh = {
        viewModel.handleUiEvent(CityScreenUiEvents.LoadData)
      }
    )
  Box(
    modifier = modifier
      .fillMaxSize()
      .pullRefresh(pullRefreshState)
  ) {
    when (val state = uiState.value) {
      is CityScreenState.Error -> {
        Column(
          modifier =
            Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          AsyncImage(
            R.drawable.error,
            contentDescription = null,
            modifier =
              Modifier
                .size(70.dp)
          )
          if (state.message.isNotBlank()) {
            Text(
              state.message,
              style = MaterialTheme.typography.bodyLarge,
              modifier = Modifier.padding(top = 16.dp, bottom = 18.dp)
            )
          }

          Text(
            stringResource(R.string.please_pull_to_refresh),
            style = MaterialTheme.typography.titleSmall
          )
        }
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

    PullRefreshIndicator(
      refreshing = false,
      state = pullRefreshState,
      modifier = Modifier.align(Alignment.TopCenter)
    )
  }
}
