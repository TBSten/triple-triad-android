package primitive

import dsl.alias
import dsl.android
import dsl.library
import dsl.libs
import dsl.plugin
import dsl.plugins
import dsl.roborazzi
import dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies

open class RoborazziPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                alias(libs.plugin("roborazzi"))
                apply(RobolectricPlugin::class)
            }
            android {
                @Suppress("UnstableApiUsage")
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                        all {
                            it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                        }
                    }
                }
            }
            dependencies {
                testImplementation(libs.library("roborazziCore"))
                testImplementation(libs.library("roborazziCompose"))
                testImplementation(libs.library("roborazziJunit"))
            }

            roborazzi {
                val projectDir = target.path.split(":").filter { it.isNotEmpty() }.joinToString("/")
                val outputDirectory = rootProject.layout.projectDirectory.dir("build/roborazzi-outputs").dir(projectDir)
                logger.info("roborazzi.outputDir = $outputDirectory")
                outputDir = outputDirectory
                compare.outputDir = outputDirectory
            }

            configureComposePreviewTests()
        }
    }
}

private fun Project.configureComposePreviewTests() {
    dependencies {
        testImplementation(libs.library("roborazziComposePreviewScannerSupport"))
        testImplementation(libs.library("composePreviewScanner"))
        if (this@configureComposePreviewTests.path != ":ui:testing") {
            testImplementation(rootProject.project(":ui:testing"))
        }
    }

    roborazzi {
        generateComposePreviewRobolectricTests {
            enable = true
            packages = provider { listOf(android.namespace) }
            robolectricConfig =
                mapOf(
                    "sdk" to "[32]",
                    "qualifiers" to "RobolectricDeviceQualifiers.Pixel5",
                )
            includePrivatePreviews = true
            testerQualifiedClassName = "me.tbsten.tripleTriad.ui.testing.AppComposePreviewTester"
        }
    }
}
