package com.itstanany.cityfinder.presentation.stateHolder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itstanany.cityfinder.R
import com.itstanany.cityfinder.domain.model.AppExceptions
import com.itstanany.cityfinder.domain.model.Result
import com.itstanany.cityfinder.domain.usecase.GetAllCitiesUseCase
import com.itstanany.cityfinder.domain.usecase.SearchCitiesByPrefixUseCase
import com.itstanany.cityfinder.presentation.model.CityGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CityScreenViewModel @Inject constructor(
  private val getAllCitiesUseCase: GetAllCitiesUseCase,
  private val searchCitiesByPrefixUseCase: SearchCitiesByPrefixUseCase,
  @ApplicationContext private val context: Context,
): ViewModel() {
  private val _uiState = MutableStateFlow<CityScreenState>(CityScreenState.Idle)
  val uiState = _uiState.asStateFlow()

  fun handleUiEvent(events: CityScreenUiEvents) {
    when (events) {
      CityScreenUiEvents.RefreshData -> {
        when (_uiState.value) {
          is CityScreenState.Error, CityScreenState.Idle -> {
            handleUiEvent(CityScreenUiEvents.LoadData)
          }

          CityScreenState.Loading -> {
            // do nothing, already loading data
          }

          is CityScreenState.Success -> {
            _uiState.update {
              (_uiState.value as CityScreenState.Success).copy(isRefreshing = true)
            }
            // here we improved UX by refreshing the screen preserving its current state
            // either regular showing of data or search state (search query)
            if ((_uiState.value as CityScreenState.Success).query.isNotBlank()) {
              handleUiEvent(CityScreenUiEvents.SearchQueryChanged((_uiState.value as CityScreenState.Success).query))
            } else {
              loadData()
            }
          }
        }
      }

      is CityScreenUiEvents.LoadData -> {
        _uiState.value = CityScreenState.Loading
        loadData()
      }

      is CityScreenUiEvents.SearchQueryChanged -> {
        _uiState.update {
          if (it is CityScreenState.Success) {
            it.copy(
              query = events.query,
              isLoadingSearch = true
              )
          } else {
            it
          }
        }
        viewModelScope.launch {
          val result = searchCitiesByPrefixUseCase(events.query)
          when (result) {
            is Result.Error -> {
              _uiState.update {
                CityScreenState.Error(getErrMsg(result.exception))
              }
            }

            is Result.Success -> {
              if (_uiState.value is CityScreenState.Success) {
                _uiState.update {
                  (it as CityScreenState.Success).copy(
                    cities = result.data
                      .groupBy { it.name.first().uppercaseChar() }
                      .map { (letter, cities) -> CityGroup(letter, cities.toImmutableList()) }
                      // if we don't have invariant that the result is always sorted
                      //,sortedBy { it.letter }
                      .toImmutableList(),
                    totalCities = result.data.size,
                    isLoadingSearch = false,
                    isRefreshing = false
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * Loads city data from the `getAllCitiesUseCase`.
   *
   * This function launches a coroutine in the `viewModelScope` to asynchronously fetch city data.
   * On successful data retrieval:
   *  - The data is grouped by the first letter of the city name.
   *  - If the current UI state is already `Success`, it updates the existing state with the new data,
   *    total city count, and sets `isRefreshing` to false.
   *  - Otherwise, it sets the UI state to a new `Success` state with the fetched data and total city count.
   * On error:
   *  - It updates the UI state to `Error` with an appropriate error message obtained from `getErrMsg`.
   */
  private fun loadData() {
    viewModelScope.launch {
      val result = getAllCitiesUseCase()
      when (result) {
        is Result.Error -> {
          _uiState.update {
            CityScreenState.Error(getErrMsg(result.exception))
          }
        }

        is Result.Success -> {
          val mappedResult = result
            .data
            .groupBy { it.name.first().uppercaseChar() }
            .map { (letter, cities) -> CityGroup(letter, cities.toImmutableList()) }
            // if we don't have invariant that the result is always sorted
            //,sortedBy { it.letter }
            .toImmutableList()

          if (_uiState.value is CityScreenState.Success) {
            _uiState.update {
              (it as CityScreenState.Success).copy(
                cities = mappedResult,
                totalCities = result.data.size,
                isRefreshing = false
              )
            }
          } else {
            _uiState.value =
              CityScreenState.Success(cities = mappedResult, totalCities = result.data.size)
          }
        }
      }
    }
  }

  /**
   * Returns a user-friendly error message based on the provided [AppExceptions].
   *
   * @param exception The [AppExceptions] instance representing the error.
   * @return A [String] containing the error message.
   *         - For [AppExceptions.FileLoadingException], it returns a predefined string for file loading errors.
   *         - For [AppExceptions.ParsingException], it returns a predefined string for data parsing errors.
   *         - For [AppExceptions.OtherException], it returns the exception's message, or an empty string if the message is null.
   */
  private fun getErrMsg(exception: AppExceptions) = when (exception) {
    is AppExceptions.FileLoadingException -> {
      context.getString(R.string.error_in_loading_data_please_try_again)
    }

    is AppExceptions.OtherException -> {
      exception.message ?: ""
    }

    is AppExceptions.ParsingException -> {
      context.getString(R.string.error_in_parsing_data_please_try_again)
    }
  }
}
