package me.tbsten.tripleTriad.ui.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import me.tbsten.tripleTriad.error.AppException
import me.tbsten.tripleTriad.error.ErrorHandleType
import me.tbsten.tripleTriad.error.ErrorState
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.R
import me.tbsten.tripleTriad.ui.ValuesPreviewParameterProvider
import me.tbsten.tripleTriad.ui.designSystem.components.AlertDialog

@Composable
internal fun CommonErrorDialog(
    errorState: ErrorState.HandleError,
    errorHandleType: ErrorHandleType.Dialog,
    onClose: () -> Unit,
    onRetry: () -> Unit,
) {
    // FIXME リトライ処理対応によって ErrorHandleType.Dialog.retry が null ではなくなるため Suppress を外すことができる
    @Suppress("SENSELESS_COMPARISON")
    val retryable = errorHandleType.retry != null

    AlertDialog(
        onDismissRequest = onClose,
        title = errorState.exception.displayTitle,
        text = errorState.exception.displayText,
        confirmButtonText = if (retryable) "再読み込み" else "閉じる",
        onConfirmClick = onRetry,
        dismissButtonText = "閉じる",
    )
}

@Suppress("ComposeUnstableReceiver")
private val Throwable.displayTitle: String
    @Composable
    get() = when (this) {
        is AppException -> when (this) {
            is AppException.Api -> stringResource(R.string.common_error_dialog_title_api)
            is AppException.Api.NoBody -> stringResource(R.string.common_error_dialog_title_api_no_body)
        }
        else -> stringResource(R.string.common_error_dialog_title_unknown)
    }

@Suppress("ComposeUnstableReceiver")
private val Throwable.displayText: String
    @Composable
    get() = when (this) {
        is AppException -> when (this) {
            is AppException.Api -> stringResource(R.string.common_error_dialog_text_api)
            is AppException.Api.NoBody -> stringResource(R.string.common_error_dialog_text_api_no_body)
        }
        else -> stringResource(R.string.common_error_dialog_text_unknown)
    }

private const val NOT_FOUND_STATUS_CODE = 404

class CommonErrorDialogPreviewParameters :
    ValuesPreviewParameterProvider<ErrorState.HandleError>(
        ErrorState.HandleError(
            AppException.Api("Not Found", NOT_FOUND_STATUS_CODE, null),
            ErrorHandleType.Dialog,
        ),
        ErrorState.HandleError(
            AppException.Api.NoBody(),
            ErrorHandleType.Dialog,
        ),
        ErrorState.HandleError(
            Exception("Normal Exception"),
            ErrorHandleType.Dialog,
        ),
    )

@Preview
@Composable
private fun CommonErrorDialogPreview(
    @PreviewParameter(CommonErrorDialogPreviewParameters::class)
    errorState: ErrorState.HandleError,
) = PreviewRoot {
    CommonErrorDialog(
        errorState = errorState,
        errorHandleType = errorState.handleType as ErrorHandleType.Dialog,
        onClose = { },
        onRetry = { },
    )
}
