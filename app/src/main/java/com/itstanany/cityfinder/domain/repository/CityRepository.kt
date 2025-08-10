package com.itstanany.cityfinder.domain.repository

import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.domain.model.Result

interface CityRepository {

  /**
   * Retrieves all available cities from the data source.
   *
   * This function asynchronously fetches all cities and returns them in the domain model format.
   *
   * @return A [Result] object. On success, it contains a list of [City] objects representing all available cities.
   *         On failure, it contains an error detailing what went wrong.
   */
  suspend fun getAllCities(): Result<List<City>>

  /**
   * Performs a case-insensitive search for cities whose names start with the given prefix.
   *
   * This function asynchronously searches for cities based on the provided prefix.
   * The search is not sensitive to the case of the letters in the prefix or the city names.
   *
   * @param prefix The prefix string to search for. If the prefix is empty, an empty list will be returned.
   * @return A [Result] object containing a list of [City] objects whose names start with the given prefix.
   *         If no cities match the prefix, or if an error occurs during the search, the [Result] will indicate this.
   *         Specifically, if the prefix is empty or no cities are found, the list within the successful [Result] will be empty.
   */
  suspend fun searchCitiesByPrefix(prefix: String): Result<List<City>>
}
