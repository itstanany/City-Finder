package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.presentation.utils.openLocationInMaps
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CityScreenContent(
  items: ImmutableList<City>,
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
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      items(items.size) { index ->
        Text(
          text = items[index].name,
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
              openLocationInMaps(
                context,
                items[index].latitude,
                items[index].longitude,
                items[index].name
              )
            }
        )
      }
    }
  }
}