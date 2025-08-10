package com.itstanany.cityfinder.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.itstanany.cityfinder.data.model.CityResponse
import okio.FileNotFoundException


class CityLocalDataSource(private val context: Context) {

  /**
   * Loads the city list JSON file from the assets folder and parses it into a list of [CityResponse] objects.
   *
   * This function reads the specified JSON file from the application's assets,
   * then uses Gson to deserialize the JSON content into a list of [CityResponse] data objects.
   *
   * @param fileName The name of the JSON file located in the assets folder.
   *                 If not provided, it defaults to "cities_sorted.json".
   * @return A [List] of [CityResponse] objects parsed from the JSON file.
   * @throws FileNotFoundException if the specified `fileName` cannot be found in the assets folder.
   * @throws JsonParseException if there is an error during the JSON parsing process (e.g., malformed JSON).
   * @throws Exception for any other unexpected errors that may occur during file reading or processing.
   */
  fun loadCitiesFromAssets(fileName: String = "cities_sorted.json"): List<CityResponse> {
    try {
      val inputStream = context.assets.open(fileName)
      val jsonString = inputStream.bufferedReader().use { it.readText() }
      val gson = Gson()
      val listType = object : TypeToken<List<CityResponse>>() {}.type
      return gson.fromJson(jsonString, listType)
    } catch (e: FileNotFoundException) {
      throw FileNotFoundException("File not found: $fileName")
    } catch (e: JsonParseException) {
      throw JsonParseException("Error parsing JSON file: $fileName")
    } catch (e: Exception) {
      throw e
    }
  }
}
