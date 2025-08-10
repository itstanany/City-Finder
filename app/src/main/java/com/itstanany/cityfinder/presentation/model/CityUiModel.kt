package com.itstanany.cityfinder.presentation.model

import com.itstanany.cityfinder.domain.model.City
import kotlinx.collections.immutable.ImmutableList

data class CityUiModel(
  val name: String,
  val countryCode: String,
  val latitude: Double,
  val longitude: Double,
  val flagRes: Int
)

data class CityGroup(
  val letter: Char,
  val cities: ImmutableList<City>
)
