package com.itstanany.cityfinder.presentation.model

import com.itstanany.cityfinder.domain.model.City
import kotlinx.collections.immutable.persistentListOf

val city1Mock = City(name = "New York", countryCode = "US", latitude = 40.7128, longitude = -74.0060)
val city2Mock = City(name = "London", countryCode = "GB", latitude = 51.5074, longitude = -0.1278)
val city3Mock = City(name = "Paris", countryCode = "FR", latitude = 48.8566, longitude = 2.3522)

val cityGroupN = CityGroup(
  letter = 'N',
  cities = persistentListOf(city1Mock)
)
val cityGroupL = CityGroup(
  letter = 'L',
  cities = persistentListOf(city2Mock)
)
val cityGroupP = CityGroup(
  letter = 'P',
  cities = persistentListOf(city3Mock)
)
val itemsMock = persistentListOf(cityGroupN, cityGroupL, cityGroupP)
