package primitive

import dsl.android
import dsl.library
import dsl.libs
import dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

open class RobolectricPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            android {
                @Suppress("UnstableApiUsage")
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }
            dependencies {
                testImplementation(libs.library("junit"))
                testImplementation(libs.library("robolectric"))
            }
        }
    }
}
