package me.tbsten.tripleTriad.error

sealed interface ErrorState {
    data object NoError : ErrorState

    data class HandleError(val exception: Throwable, val handleType: ErrorHandleType) : ErrorState
    data class Hide(val handleError: HandleError) : ErrorState
}
