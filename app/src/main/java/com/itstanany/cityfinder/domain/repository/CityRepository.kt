package com.itstanany.cityfinder.domain.repository

import com.itstanany.cityfinder.domain.model.City

interface CityRepository {

  /**
   * Returns all available cities in the domain model format.
   *
   * @return A list of [City] objects representing all available cities.
   */
  suspend fun getAllCities(): List<City>

  /**
   * Performs a case-insensitive search for cities whose names start with the given prefix.
   *
   * @param prefix The prefix string to search for.
   * @return A list of [City] objects that match the prefix. Returns an empty list if no cities match or the prefix is empty.
   */
  suspend fun searchCitiesByPrefix(prefix: String): List<City>
}
