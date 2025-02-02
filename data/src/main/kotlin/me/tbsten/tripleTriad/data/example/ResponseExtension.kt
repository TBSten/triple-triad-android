package me.tbsten.tripleTriad.data.example

import me.tbsten.tripleTriad.error.AppException
import retrofit2.Response

fun <B> Response<B>.bodyOrThrow(): B = if (this.isSuccessful) {
    this.body() ?: throw AppException.Api.NoBody()
} else {
    throw AppException.Api("API call not success. message: ${message()}", this.code(), null)
}
