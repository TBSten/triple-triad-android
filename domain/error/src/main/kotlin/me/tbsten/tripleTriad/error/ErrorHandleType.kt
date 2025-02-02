package me.tbsten.tripleTriad.error

sealed interface ErrorHandleType {
    val retry: (() -> Unit)?

    /**
     * エラーが発生した場合共通のエラーダイアログを表示する
     */
    data object Dialog : ErrorHandleType {
        override val retry = null
    }

    /**
     * エラーが発生しても無視する
     */
    data object Ignore : ErrorHandleType {
        override val retry = null
    }
}
