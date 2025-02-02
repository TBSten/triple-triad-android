package your.projectPackage.error

sealed class AppException(
    val logMessage: String?,
    override val cause: Throwable?,
) : Exception() {
    class Api(
        logMessage: String?,
        statusCode: Int,
        override val cause: Throwable?,
    ) : AppException("API Error message:$logMessage statusCode:$statusCode", cause) {
        class NoBody : AppException("API call not success. message", null)
    }
}
