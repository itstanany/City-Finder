package com.itstanany.cityfinder.data.repository

import com.itstanany.cityfinder.data.datasource.CityLocalDataSource
import com.itstanany.cityfinder.data.mapper.toDomain
import com.itstanany.cityfinder.data.trie.CityTrie
import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.domain.repository.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CityRepositoryImpl @Inject constructor(
  private val localDataSource: CityLocalDataSource
) : CityRepository {

  private val cityTrie = CityTrie()
  private lateinit var allCities: List<City>

  /**
   * Initializes the repository by loading cities from the local data source,
   * converting them to domain models, and inserting them into the Trie for efficient searching.
   * This function is executed on the [Dispatchers.Default] coroutine dispatcher.
   */
  private suspend fun initialize() = withContext(Dispatchers.Default) {
    val dataModelCities = localDataSource.loadCitiesFromAssets()
    allCities = dataModelCities.map { it.toDomain() }
    for (city in allCities) {
      cityTrie.insert(city)
    }
  }

  // documentation added to abstract signature
  override suspend fun getAllCities(): List<City> = withContext(Dispatchers.Default) {
    if (!::allCities.isInitialized) {
      initialize()
    }
    allCities
  }

  // documentation added to abstract signature
  override suspend fun searchCitiesByPrefix(prefix: String): List<City> =
    withContext(Dispatchers.Default) {
      if (!::allCities.isInitialized) {
        initialize()
      }
      if (prefix.isBlank()) {
        return@withContext allCities
      }
      val matchedCities = cityTrie.search(prefix)
      return@withContext matchedCities
      // IMPORTANT NOTE
      // Sort by city name then country code, case-insensitive
      // but in our case the raw file is pre-processed as sorted
      /*
      matchedCities.sortedWith(
        compareBy<City> { it.name.lowercase() }
          .thenBy { it.countryCode.lowercase() }
      )
       */
    }
}