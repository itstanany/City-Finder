package com.itstanany.cityfinder.domain.usecase

import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.domain.repository.CityRepository
import jakarta.inject.Inject

class SearchCitiesByPrefixUseCase @Inject constructor(
  private val cityRepository: CityRepository
) {
  suspend operator fun invoke(prefix: String): List<City> {
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