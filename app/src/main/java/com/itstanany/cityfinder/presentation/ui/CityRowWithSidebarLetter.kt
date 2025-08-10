package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.presentation.model.city1Mock
import com.itstanany.cityfinder.ui.theme.CityFinderTheme

/**
 * A Composable function that displays a row for a city with a sidebar letter.
 *
 * This Composable is used to display each city in the list. It shows the city's country code
 * in a circular badge, the city name, country code, and its latitude and longitude.
 * A vertical line is drawn on the left side, acting as a visual sidebar element.
 * The entire row is clickable.
 *
 * @param city The [City] object containing the data to display.
 * @param onClick A lambda function to be executed when the city row is clicked.
 */
@Composable
fun CityRowWithSidebarLetter(
  city: City,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxHeight()
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(start = 28.dp)
      .drawBehind {
        val centerX = size.width / size.width
        val startY = 0f
        val endY = size.height
        drawLine(
          color = Color.Black,
          start = Offset(centerX, startY),
          end = Offset(centerX, size.height),
          strokeWidth = 2.5f
        )
      }
      .padding(top = 0.dp, bottom = 0.dp, start = 20.dp),
    contentAlignment = Alignment.TopCenter
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .background(Color.White, shape = MaterialTheme.shapes.small)
        .padding(horizontal = 8.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
    ) {
      Box(
        modifier = Modifier
          .padding(end = 8.dp)
          .size(70.dp)
          .background(Color.LightGray, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = city.countryCode.uppercase(),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Black
          ),
        )
      }
      Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
      ) {
        Text(
          text = "${city.name}, ${city.countryCode}",
          style = MaterialTheme.typography.titleMedium
        )
        Text(
          text = "${city.latitude}, ${city.longitude}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun CityRowWithSidebarLetterPreview() {
  CityFinderTheme {
    CityRowWithSidebarLetter(city = city1Mock, onClick = {})
  }
}
