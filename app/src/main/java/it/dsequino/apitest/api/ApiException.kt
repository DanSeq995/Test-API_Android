package it.dsequino.apitest.api

class ApiException(val errorCode: Int, val errorMessage: String) : Throwable()
