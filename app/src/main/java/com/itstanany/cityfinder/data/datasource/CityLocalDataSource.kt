package com.itstanany.cityfinder.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itstanany.cityfinder.data.model.CityResponse


class CityLocalDataSource(private val context: Context) {

  /**
   * Loads the city list JSON file from the assets folder and parses it into a list of [CityResponse] objects.
   *
   * @param fileName The name of the JSON file in the assets folder. Defaults to "cities_sorted.json".
   * @return A list of [CityResponse] objects parsed from the JSON file.
   * @throws java.io.IOException if the file cannot be opened or read.
   */
  fun loadCitiesFromAssets(fileName: String = "cities_sorted.json"): List<CityResponse> {
    val inputStream = context.assets.open(fileName)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    val listType = object : TypeToken<List<CityResponse>>() {}.type
    return gson.fromJson(jsonString, listType)
  }
}
