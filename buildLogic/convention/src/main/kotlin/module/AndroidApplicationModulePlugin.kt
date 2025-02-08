package module

import dsl.alias
import dsl.androidApplication
import dsl.androidTestImplementation
import dsl.debugImplementation
import dsl.implementation
import dsl.library
import dsl.libs
import dsl.plugin
import dsl.plugins
import dsl.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import primitive.ComposePlugin
import primitive.HiltPlugin

open class AndroidApplicationModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                alias(libs.plugin("androidApplication"))
            }

            configureBasic()
            configureAndroid()

            plugins {
                apply(ComposePlugin::class)
                apply(HiltPlugin::class)
            }

            androidApplication {
                defaultConfig {
                    targetSdk =
                        libs
                            .version("app-targetSdk")
                            .toInt()
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        merges += "META-INF/LICENSE.md"
                        merges += "META-INF/LICENSE-notice.md"
                    }
                }
            }

            dependencies {
                implementation(libs.library("activityCompose"))
                implementation(libs.library("composeUi"))
                implementation(libs.library("composeUiGraphics"))
                implementation(libs.library("composeUiToolingPreview"))

                androidTestImplementation(libs.library("composeUiTestJunit4"))

                debugImplementation(libs.library("composeUiTooling"))
                debugImplementation(libs.library("composeUiTestManifest"))
            }
        }
    }
}
