package your.projectPackage.ui.error

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import your.projectPackage.error.AppException
import your.projectPackage.error.ErrorHandleType
import your.projectPackage.error.ErrorState
import your.projectPackage.ui.PreviewRoot
import your.projectPackage.ui.R
import your.projectPackage.ui.ValuesPreviewParameterProvider
import your.projectPackage.ui.component.AppButton

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
        title = {
            Text(errorState.exception.displayTitle)
        },
        text = errorState.exception.displayText.let { { Text(it) } },
        confirmButton = {
            @Suppress("KotlinConstantConditions")
            if (retryable) {
                AppButton(onClick = onRetry) {
                    Text("再読み込み")
                }
            } else {
                AppButton(onClick = onClose) {
                    Text("閉じる")
                }
            }
        },
        dismissButton = @Suppress("KotlinConstantConditions") if (retryable) {
            {
                AppButton(onClick = onClose) {
                    Text("閉じる")
                }
            }
        } else {
            null
        },
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
