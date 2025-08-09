package com.itstanany.cityfinder.data.model

data class CityResponse(
  val country: String,
  val name: String,
  val _id: Long,
  val coord: CoordResponse
)
