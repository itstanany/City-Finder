package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.itstanany.cityfinder.R
import com.itstanany.cityfinder.presentation.model.CityGroup
import com.itstanany.cityfinder.presentation.model.itemsMock
import com.itstanany.cityfinder.presentation.utils.openLocationInMaps
import com.itstanany.cityfinder.ui.theme.CityFinderTheme
import kotlinx.collections.immutable.ImmutableList

/**
 * Composable function that displays the content of the city screen.
 *
 * This function displays a list of cities, grouped by their starting letter,
 * along with a search bar to filter the cities.
 *
 * @param items The list of city groups to display.
 * @param totalCities The total number of cities available.
 * @param onQueryChanged A callback function that is invoked when the search query changes.
 * @param searchQuery The current search query.
 * @param modifier The modifier to apply to this composable.
 * @param isLoadingSearch A boolean indicating whether the search is currently loading.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityScreenContent(
  items: ImmutableList<CityGroup>,
  totalCities: Int,
  onQueryChanged: (String) -> Unit,
  searchQuery: String,
  modifier: Modifier = Modifier,
  isLoadingSearch: Boolean = false
) {
  val context = LocalContext.current

  Scaffold(
    modifier = modifier.fillMaxSize(),
    bottomBar = {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = {
          onQueryChanged(it)
        },
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.onPrimary)
          .padding(16.dp),
        placeholder = { Text(stringResource(R.string.search)) },
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        leadingIcon = {
          AsyncImage(
            model = R.drawable.ic_search,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
        },
        colors = OutlinedTextFieldDefaults.colors().copy(
          unfocusedContainerColor = MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.medium
      )
    }
  ) { innerPadding ->
    if (isLoadingSearch) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        CircularProgressIndicator(
          modifier = Modifier
            .align(androidx.compose.ui.Alignment.Center)
        )
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .padding(innerPadding)
          .padding(horizontal = 12.dp)
          .fillMaxSize()
      ) {
        item {
          Column {
            Text(
              stringResource(R.string.city_search),
              style = MaterialTheme.typography.headlineLarge.copy(fontWeight = Bold),
              modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
              stringResource(R.string.cities, totalCities),
              textAlign = TextAlign.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp)
            )
          }
        }

        items.forEach { group ->
          stickyHeader {
            SidebarHeader(
              letter = group.letter
            )
          }
          items(group.cities.size) { index ->
            CityRowWithSidebarLetter(
              city = group.cities[index],
              onClick = {
                openLocationInMaps(
                  context,
                  group.cities[index].latitude,
                  group.cities[index].longitude,
                  group.cities[index].name
                )
              }
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun CityScreenContentPreview() {
  CityFinderTheme {
    CityScreenContent(
      items = itemsMock,
      totalCities = 3,
      onQueryChanged = {},
      searchQuery = ""
    )
  }
}
