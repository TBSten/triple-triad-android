package your.projectPackage.data.example

import retrofit2.Response
import your.projectPackage.error.AppException

fun <B> Response<B>.bodyOrThrow(): B = if (this.isSuccessful) {
    this.body() ?: throw AppException.Api.NoBody()
} else {
    throw AppException.Api("API call not success. message: ${message()}", this.code(), null)
}
