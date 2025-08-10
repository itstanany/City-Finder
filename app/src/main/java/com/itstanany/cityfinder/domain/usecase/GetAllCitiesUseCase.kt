package com.itstanany.cityfinder.domain.usecase

import com.itstanany.cityfinder.domain.model.City
import com.itstanany.cityfinder.domain.model.Result
import com.itstanany.cityfinder.domain.repository.CityRepository
import jakarta.inject.Inject

class GetAllCitiesUseCase @Inject constructor(
  private val cityRepository: CityRepository
) {
  suspend operator fun invoke(): Result<List<City>> {
    return cityRepository.getAllCities()
    // IMPORTANT NOTE:
    // enhancement if we don't have a guarantee that the result is sorted by name from the source
    // in this project, we pre-processed the data to be sorted alphabetically by city name
    /*
      return repository.getAllCities()
                  .sortedWith(compareBy<City> { it.name.lowercase() }
                      .thenBy { it.countryCode.lowercase() })
     */
  }

}