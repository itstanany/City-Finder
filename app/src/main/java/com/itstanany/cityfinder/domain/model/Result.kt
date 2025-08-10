package com.itstanany.cityfinder.domain.model

sealed class Result<out T> {
  data class Success<T>(val data: T) : Result<T>()
  data class Error(val exception: AppExceptions) : Result<Nothing>()
}
