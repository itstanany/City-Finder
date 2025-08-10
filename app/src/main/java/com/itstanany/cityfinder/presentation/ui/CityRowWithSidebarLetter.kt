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
import com.itstanany.cityfinder.domain.model.City

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
