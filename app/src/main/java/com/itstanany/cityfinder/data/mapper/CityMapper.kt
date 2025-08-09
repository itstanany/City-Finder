package com.itstanany.cityfinder.data.mapper

import com.itstanany.cityfinder.data.model.CityResponse
import com.itstanany.cityfinder.domain.model.City

fun CityResponse.toDomain(): City =
  City(
    name = this.name,
    countryCode = this.country,
    latitude = this.coord.lat,
    longitude = this.coord.lon
  )
