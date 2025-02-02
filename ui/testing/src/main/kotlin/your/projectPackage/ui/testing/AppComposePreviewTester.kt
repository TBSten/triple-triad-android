package your.projectPackage.ui.testing

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.AndroidComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziActivity
import com.github.takahirom.roborazzi.RoborazziRecordFilePathStrategy
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.provideRoborazziContext
import com.github.takahirom.roborazzi.roborazziDefaultNamingStrategy
import com.github.takahirom.roborazzi.roborazziRecordFilePathStrategy
import com.github.takahirom.roborazzi.roborazziSystemPropertyOutputDirectory
import com.github.takahirom.roborazzi.toRoborazziComposeOptions
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.android.screenshotid.AndroidPreviewScreenshotIdBuilder
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview

@Suppress("unused")
@OptIn(ExperimentalRoborazziApi::class)
class AppComposePreviewTester : ComposePreviewTester<AndroidPreviewInfo> by AndroidComposePreviewTester() {
    private val composeTestRule = createAndroidComposeRule<RoborazziActivity>()

    override fun options(): ComposePreviewTester.Options {
        val testLifecycleOptions = ComposePreviewTester.Options.JUnit4TestLifecycleOptions(
            testRuleFactory = { composeTestRule },
        )
        return super.options().copy(testLifecycleOptions = testLifecycleOptions)
    }

    @OptIn(InternalRoborazziApi::class)
    override fun test(preview: ComposablePreview<AndroidPreviewInfo>) = runTest {
        val filePath =
            run {
                val isRelativePathFromCurrentDirectory =
                    roborazziRecordFilePathStrategy() ==
                        RoborazziRecordFilePathStrategy.RelativePathFromCurrentDirectory

                val pathPrefix =
                    if (isRelativePathFromCurrentDirectory) {
                        roborazziSystemPropertyOutputDirectory() + java.io.File.separator
                    } else {
                        ""
                    }
                val name = roborazziDefaultNamingStrategy().generateOutputName(
                    preview.declaringClass,
                    createScreenshotIdFor(preview),
                )
                "$pathPrefix$name.${provideRoborazziContext().imageExtension}"
            }

        // Preview アノテーションの内容を反映する
        val previewContent =
            preview
                .toRoborazziComposeOptions()
                .configured(composeTestRule.activityRule.scenario) {
                    preview()
                }
        composeTestRule.setContent {
            previewContent()
        }

        // ローディングインジケータなどを含む Preview で想定以上の時間がかかるため、最大 10秒 待つようにする。
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.awaitIdle(maxDuration = 10.seconds)
            .also {
                if (it) println("WARN: Cancelled wait because ComposeTestRule.awaitIdle() took more than 10 seconds.")
            }

        composeTestRule.onRoot()
            .captureRoboImage(filePath)
    }

    private fun createScreenshotIdFor(
        preview: ComposablePreview<AndroidPreviewInfo>,
    ) = AndroidPreviewScreenshotIdBuilder(preview)
        .ignoreClassName()
        .build()

    /**
     * @return maxDuration 以上 awaitIdle() がかかって タイムアウトされたか？
     */
    private suspend fun ComposeTestRule.awaitIdle(maxDuration: Duration): Boolean = withTimeoutOrNull(maxDuration) {
        awaitIdle()
    } != null
}
