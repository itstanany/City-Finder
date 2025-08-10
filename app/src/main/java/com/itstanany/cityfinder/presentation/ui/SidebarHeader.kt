package com.itstanany.cityfinder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SidebarHeader(letter: Char) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(56.dp)
        .background(Color.White, CircleShape)
        .border(1.dp, Color.Black, CircleShape),

      contentAlignment = Alignment.Center
    ) {
      Text(
        text = letter.toString(),
        style = MaterialTheme.typography.titleMedium,
      )
    }
    Spacer(modifier = Modifier.weight(1f)) // Empty space to align with city column
  }
}
