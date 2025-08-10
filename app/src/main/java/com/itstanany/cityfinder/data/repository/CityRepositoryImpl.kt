package com.itstanany.cityfinder.data.repository

import com.google.gson.JsonParseException
import com.itstanany.cityfinder.data.datasource.CityLocalDataSource
import com.itstanany.cityfinder.data.mapper.toDomain
import com.itstanany.cityfinder.data.trie.CityTrie
import com.itstanany.cityfinder.domain.model.AppExceptions
import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.domain.model.Result
import com.itstanany.cityfinder.domain.repository.CityRepository
import jakarta.inject.Inject
import java.io.FileNotFoundException
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
   * It populates the `allCities` list and the `cityTrie`.
   */
  private suspend fun initialize() = withContext(Dispatchers.Default) {
    val dataModelCities = localDataSource.loadCitiesFromAssets()
    allCities = dataModelCities.map { it.toDomain() }
    for (city in allCities) {
      cityTrie.insert(city)
    }
  }

  /**
   * Retrieves a list of all available cities.
   * If the cities have not been loaded yet, it will first initialize the repository.
   * This function is executed on the [Dispatchers.Default] coroutine dispatcher.
   *
   * @return A [Result] object containing either:
   *         - [Result.Success] with a list of [City] objects.
   *         - [Result.Error] with an [AppExceptions] if an error occurs during loading or processing.
   *           Possible exceptions include:
   *           - [AppExceptions.FileLoadingException] if the city data file is not found.
   *           - [AppExceptions.ParsingException] if there's an error parsing the city data.
   *           - [AppExceptions.OtherException] for any other unexpected errors.
   */
  override suspend fun getAllCities(): Result<List<City>> = withContext(Dispatchers.Default) {
    return@withContext try {
      if (!::allCities.isInitialized) {
        initialize()
      }
      Result.Success(allCities)
    } catch (e: FileNotFoundException) {
      Result.Error(AppExceptions.FileLoadingException(e.message))
    } catch (e: JsonParseException) {
      Result.Error(AppExceptions.ParsingException(e.message))
    } catch (e: Exception) {
      Result.Error(AppExceptions.OtherException(e.message))
    }
  }


  /**
   * Searches for cities whose names start with the given prefix.
   * If the cities have not been loaded yet, it will first initialize the repository.
   * If the prefix is blank, it returns all cities.
   * The search is performed using a Trie data structure for efficiency.
   * This function is executed on the [Dispatchers.Default] coroutine dispatcher.
   *
   * Note: The underlying data is pre-sorted by city name then country code (case-insensitive).
   * Therefore, additional sorting of the results is currently commented out for performance.
   *
   * @param prefix The prefix string to search for. The search is case-insensitive.
   * @return A [Result] object containing either:
   *         - [Result.Success] with a list of matching [City] objects.
   *         - [Result.Error] with an [AppExceptions] if an error occurs during initialization or searching.
   *           Possible exceptions include:
   *           - [AppExceptions.FileLoadingException] if the city data file is not found during initialization.
   *           - [AppExceptions.ParsingException] if there's an error parsing the city data during initialization.
   *           - [AppExceptions.OtherException] for any other unexpected errors.
   */
  override suspend fun searchCitiesByPrefix(prefix: String): Result<List<City>> =
    withContext(Dispatchers.Default) {
      return@withContext try {
        if (!::allCities.isInitialized) {
          initialize()
        }
        if (prefix.isBlank()) {
          Result.Success(allCities)
        } else {
          val matchedCities = cityTrie.search(prefix)
          Result.Success(matchedCities)
        }
        // IMPORTANT NOTE
        // Sort by city name then country code, case-insensitive
        // but in our case the raw file is pre-processed as sorted
        /*
        matchedCities.sortedWith(
          compareBy<City> { it.name.lowercase() }
            .thenBy { it.countryCode.lowercase() }
        )
         */
      } catch (e: FileNotFoundException) {
        Result.Error(AppExceptions.FileLoadingException(e.message))
      } catch (e: JsonParseException) {
        Result.Error(AppExceptions.ParsingException(e.message))
      } catch (e: Exception) {
        Result.Error(AppExceptions.OtherException(e.message))
      }

    }
}
