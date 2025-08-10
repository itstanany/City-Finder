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

/**
 * Composable function that represents the main screen for displaying a list of cities.
 * It observes the UI state from the [viewModel] and renders different content based on the state:
 * - Loading: Shows a circular progress indicator.
 * - Error: Displays an error message and an error icon, prompting the user to pull to refresh.
 * - Success: Renders the [CityScreenContent] with the list of cities.
 * - Idle: Renders nothing.
 *
 * The screen supports pull-to-refresh functionality to reload the city data.
 *
 * @param viewModel The [CityScreenViewModel] responsible for managing the screen's state and logic.
 * @param modifier The [Modifier] to be applied to the root Composable of this screen.
 */
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
        viewModel.handleUiEvent(CityScreenUiEvents.RefreshData)
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
            .align(Alignment.Center)
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

        if (state.isRefreshing) {
          // we can improve UX here by delaying the refreshing animation for some fraction of time
          // so if response is fast enough to not showing loading indicator, it will be better
          CircularProgressIndicator(
            modifier = Modifier
              .align(Alignment.Center)
          )
        }
      }
    }

    PullRefreshIndicator(
      refreshing = false,
      state = pullRefreshState,
      modifier = Modifier.align(Alignment.TopCenter)
    )
  }
}
