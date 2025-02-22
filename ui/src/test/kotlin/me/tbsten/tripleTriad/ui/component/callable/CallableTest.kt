package me.tbsten.tripleTriad.ui.component.callable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.assertEquals
import kotlinx.coroutines.launch
import me.tbsten.tripleTriad.ui.designSystem.components.Button
import me.tbsten.tripleTriad.ui.designSystem.components.Surface
import me.tbsten.tripleTriad.ui.designSystem.components.Text
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

private const val TestConfirmDialogText = "Are you sure you want me to delete it?"

@RunWith(AndroidJUnit4::class)
internal class CallableTest {
    val testRoboImageFilePrefix = "CallableTest"

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    val confirm = Callable<String, Boolean> { text: String ->
        Box(
            Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        end(false)
                    }
                }
                .background(Color.Black.copy(alpha = 0.25f))
                .fillMaxSize()
                .testTag("ConfirmRoot"),
            contentAlignment = Alignment.Center,
        ) {
            Surface {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .testTag("confirm-dialog"),
                ) {
                    Text(text)
                    Button(text = "cancel", onClick = { end(false) })
                    Button(text = "ok", onClick = { end(true) }, modifier = Modifier.testTag("confirm-ok"))
                }
            }
        }
    }

    @SuppressLint("ComposeUnstableReceiver", "ComposeModifierMissing")
    @Composable
    fun TestContent(onDelete: () -> Unit) {
        Box(Modifier.fillMaxSize()) {
            listOf(
                confirm,
            ).Provider {
                Column {
                    val confirmState = confirm()
                    val coroutineScope = rememberCoroutineScope()

                    var result by remember { mutableStateOf<Boolean?>(null) }

                    Button(
                        text = "delete",
                        onClick = {
                            coroutineScope.launch {
                                val delete = confirmState.call(TestConfirmDialogText)
                                    .also { result = it }
                                if (delete) onDelete()
                            }
                        },
                    )
                    Text("result: $result", modifier = Modifier.testTag("result-text"))
                }
            }
        }
    }

    fun assertExistsDialog() {
        composeTestRule.onNodeWithTag("confirm-dialog")
            .assertExists("Not exists confirm-dialog")
        composeTestRule.onNodeWithTag("confirm-ok")
            .assertExists("Exists confirm-dialog but not exists confirm-ok")
        composeTestRule.onNodeWithText(TestConfirmDialogText)
            .assertExists("Exists confirm-dialog but not exists ConfirmDialogText")
    }

    fun assertDoesExistsDialog() {
        composeTestRule.onNodeWithTag("confirm-dialog").assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestConfirmDialogText).assertDoesNotExist()
        composeTestRule.onNodeWithTag("confirm-ok").assertDoesNotExist()
    }

    @Test
    fun testBasicUsage() {
        val roboImagePrefix = "$testRoboImageFilePrefix.testBasicUsage"

        var deleted = false
        fun onDelete() {
            deleted = true
        }

        composeTestRule.setContent {
            TestContent(
                onDelete = ::onDelete,
            )
        }

        // dialog が表示されていないことを確認
        assertDoesExistsDialog()
        assertEquals(deleted, false)

        composeTestRule.onRoot().captureRoboImage("$roboImagePrefix.initial.png")

        // delete ボタンをクリック
        composeTestRule
            .onNodeWithText("delete")
            .performClick()

        // dialog が表示されていることを確認
        assertExistsDialog()
        assertEquals(deleted, false)

        composeTestRule.onRoot().captureRoboImage("$roboImagePrefix.after-click-delete.png")

        // ok ボタンをクリック
        composeTestRule
            .onNodeWithTag("confirm-ok")
            .performClick()

        // dialog が表示されていないことを確認
        assertDoesExistsDialog()
        assertEquals(deleted, true)

        composeTestRule.onNodeWithTag("result-text")
            .assertExists()
            .assertTextEquals("result: true")

        composeTestRule.onRoot().captureRoboImage("$roboImagePrefix.after-close-dialog.png")
    }

    @Test
    fun testCancel() {
        val roboImagePrefix = "$testRoboImageFilePrefix.testCancel"

        var deleted = false
        fun onDelete() {
            deleted = true
        }

        composeTestRule.setContent {
            TestContent(
                onDelete = ::onDelete,
            )
        }

        // dialog が表示されていないことを確認
        assertDoesExistsDialog()
        assertEquals(deleted, false)

        composeTestRule.onRoot().captureRoboImage("$roboImagePrefix.initial.png")

        // delete ボタンをクリック
        composeTestRule
            .onNodeWithText("delete")
            .performClick()

        // dialog が表示されていることを確認
        assertExistsDialog()
        assertEquals(deleted, false)

        composeTestRule.onRoot().captureRoboImage("$roboImagePrefix.after-click-delete.png")

        // Backdrop をタップしてキャンセル
        composeTestRule
            .onNodeWithTag("ConfirmRoot")
            .performClick()

        // dialog が表示されていないことを確認
        assertDoesExistsDialog()
        assertEquals(deleted, false)

        composeTestRule.onNodeWithTag("result-text")
            .assertExists()
            .assertTextEquals("result: false")

        composeTestRule.onRoot().captureRoboImage("$roboImagePrefix.after-cancel.png")
    }
}
