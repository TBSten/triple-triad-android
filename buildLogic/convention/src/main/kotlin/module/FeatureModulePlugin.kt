package module

import dsl.androidTestImplementation
import dsl.debugImplementation
import dsl.implementation
import dsl.library
import dsl.libs
import dsl.plugins
import dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import primitive.ComposePlugin
import primitive.HiltPlugin
import primitive.NavigationComposePlugin

open class FeatureModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(AndroidLibraryModulePlugin::class)
                apply(ComposePlugin::class)
                apply(NavigationComposePlugin::class)
                apply(HiltPlugin::class)
            }

            dependencies {
                implementation(rootProject.project(":ui"))
                implementation(rootProject.project(":domain"))

                implementation(libs.library("composeUi"))
                implementation(libs.library("composeUiGraphics"))
                implementation(libs.library("composeUiToolingPreview"))
                implementation(libs.library("hiltNavigationCompose"))
                implementation(libs.library("androidxLifecycleViewModelKtx"))
                implementation(libs.library("tartCompose"))

                testImplementation(libs.library("junit"))

                androidTestImplementation(libs.library("androidxJunit"))
                androidTestImplementation(libs.library("androidxEspressoCore"))
                androidTestImplementation(libs.library("composeUiTestJunit4"))

                debugImplementation(libs.library("composeUiTooling"))
                debugImplementation(libs.library("composeUiTestManifest"))
            }
        }
    }
}
