package ru.ulyanaab.lifemates.common

sealed class Result<T> {

    data class Success<T>(
        val data: T,
    ) : Result<T>()

    data class Failure<T>(
        val error: Error
    ) : Result<T>()
}

sealed class Error {
    object Unauthorized : Error()
    object ServerError : Error()
    object Forbidden : Error()
    object Unknown : Error()
}
