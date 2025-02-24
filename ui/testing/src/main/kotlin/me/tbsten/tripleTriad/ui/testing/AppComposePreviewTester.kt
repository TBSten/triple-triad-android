package me.tbsten.tripleTriad.ui.testing

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.AndroidComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziActivity
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.roborazziDefaultNamingStrategy
import com.github.takahirom.roborazzi.toRoborazziComposeOptions
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
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

    override fun previews(): List<ComposablePreview<AndroidPreviewInfo>> {
        val options = options()
        return AndroidComposablePreviewScanner()
            .scanPackageTrees(*options.scanOptions.packages.toTypedArray())
            .excludeIfAnnotatedWithAnyOf(IgnoreVrt::class.java)
            .let {
                if (options.scanOptions.includePrivatePreviews) {
                    it.includePrivatePreviews()
                } else {
                    it
                }
            }.getPreviews()
    }

    override fun test(preview: ComposablePreview<AndroidPreviewInfo>) = runTest(timeout = 10.seconds) {
        val filePath =
            run {
                val name = roborazziDefaultNamingStrategy().generateOutputName(
                    preview.declaringClass,
                    createScreenshotIdFor(preview),
                )
                getRoborazziOutputFilePath(name)
            }

        withTimeout(6.seconds) {
            // 無限ループする Composable も考慮に入れて
            // 一番最初のフレームのみキャプチャする
            composeTestRule.mainClock.autoAdvance = false

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
