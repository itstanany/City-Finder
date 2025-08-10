package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.itstanany.cityfinder.presentation.model.CityGroup
import com.itstanany.cityfinder.presentation.utils.openLocationInMaps
import kotlin.math.min
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityScreenContent(
  items: ImmutableList<CityGroup>,
  onQueryChanged: (String) -> Unit,
  searchQuery: String,
  modifier: Modifier = Modifier
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
          .padding(16.dp),
        label = { Text("Search") }
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .padding(innerPadding)
        .padding(horizontal = 12.dp)
        .fillMaxSize()
    ) {
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
