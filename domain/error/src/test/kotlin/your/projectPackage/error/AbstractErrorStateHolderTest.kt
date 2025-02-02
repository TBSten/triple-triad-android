package your.projectPackage.error

import kotlin.test.assertEquals
import org.junit.Test

class AbstractErrorStateHolderTest {
    @Test
    fun testSendErrorState() {
        val exception = Exception()
        val errorStateHolder = TestErrorStateHolder()

        assert(errorStateHolder.errorState.value == ErrorState.NoError)

        val newErrorState = ErrorState.HandleError(exception, ErrorHandleType.Dialog)
        errorStateHolder.sendErrorState(newErrorState)
        assertEquals(errorStateHolder.errorState.value, newErrorState)
    }

    @Test
    fun testOnCloseState() {
        val exception = Exception()
        val errorStateHolder = TestErrorStateHolder()

        assertEquals(errorStateHolder.errorState.value, ErrorState.NoError)

        val newErrorState = ErrorState.HandleError(exception, ErrorHandleType.Dialog)
        errorStateHolder.sendErrorState(newErrorState)
        assertEquals(errorStateHolder.errorState.value, newErrorState)

        errorStateHolder.onClose()

        assertEquals(errorStateHolder.errorState.value, ErrorState.Hide(newErrorState))
    }
}

private class TestErrorStateHolder : AbstractErrorStateHolder()
