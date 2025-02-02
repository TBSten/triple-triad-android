package me.tbsten.tripleTriad.error

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LaunchSafeTest {
    @Test
    fun testBasicLaunchSafe() = runTest {
        val expectException = ExpectedException()
        val errorStateHolder = FakeErrorStateHolder()

        val supervisorJob = SupervisorJob()
        val scope = CoroutineScope(supervisorJob)

        with(LaunchSafe(errorStateHolder)) {
            scope.launchSafe(handleType = ErrorHandleType.Dialog) {
                throw expectException
            }.join()
        }

        assert(!supervisorJob.isCompleted) { "例えエラーが発生しても、supervisorJob が終了してはいけない" }
        assert(!supervisorJob.isCancelled) { "例えエラーが発生しても、supervisorJob はキャンセルされてはいけない" }

        val errorState = errorStateHolder.errorState.value
        assert(errorState == ErrorState.HandleError(expectException, ErrorHandleType.Dialog))
    }

    @Test
    fun testNestedLaunchSafe() = runTest {
        val expectException = ExpectedException()
        val errorStateHolder = FakeErrorStateHolder()

        val supervisorJob = SupervisorJob()
        val scope = CoroutineScope(supervisorJob)

        with(LaunchSafe(errorStateHolder)) {
            scope.launchSafe(handleType = ErrorHandleType.Dialog) {
                launch {
                    throw expectException
                }
            }.join()
        }

        assert(!supervisorJob.isCompleted) { "例えエラーが発生しても、supervisorJob が終了してはいけない" }
        assert(!supervisorJob.isCancelled) { "例えエラーが発生しても、supervisorJob はキャンセルされてはいけない" }

        val errorState = errorStateHolder.errorState.value
        assert(errorState == ErrorState.HandleError(expectException, ErrorHandleType.Dialog))
    }
}

private class FakeErrorStateHolder : AbstractErrorStateHolder()

private class ExpectedException : RuntimeException("expected")
