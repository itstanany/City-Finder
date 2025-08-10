package com.itstanany.cityfinder.domain.model

sealed class AppExceptions {
  data class FileLoadingException(val message: String?) : AppExceptions()
  data class ParsingException(val message: String?) : AppExceptions()
  data class OtherException(val message: String?) : AppExceptions()
}