package com.itstanany.cityfinder.domain.usecase

import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.domain.model.Result
import com.itstanany.cityfinder.domain.repository.CityRepository
import jakarta.inject.Inject

/**
 * Use case for searching cities by a given prefix.
 *
 * This use case encapsulates the business logic for retrieving a list of cities
 * whose names start with the specified prefix. It delegates the actual data
 * retrieval to a [CityRepository].
 *
 * The current implementation assumes that the underlying data source
 * (accessed via [CityRepository.searchCitiesByPrefix]) returns cities
 * already sorted alphabetically by name. If this assumption is not met,
 * the commented-out code block within the `invoke` operator can be used
 * to sort the results client-side by city name (case-insensitive) and then
 * by country code (case-insensitive) as a secondary sorting criterion.
 *
 * @param cityRepository The repository responsible for fetching city data.
 */
class SearchCitiesByPrefixUseCase @Inject constructor(
  private val cityRepository: CityRepository
) {
  suspend operator fun invoke(prefix: String): Result<List<City>> {
    return cityRepository.searchCitiesByPrefix(prefix)
    // IMPORTANT NOTE:
    // enhancement if we don't have a guarantee that the result is sorted by name from the source
    // in this project, we pre-processed the data to be sorted alphabetically by city name
    /*
    return repository.searchCitiesByPrefix(prefix)
            .sortedWith(compareBy<City> { it.name.lowercase() }
                .thenBy { it.countryCode.lowercase() })
     */
  }
}