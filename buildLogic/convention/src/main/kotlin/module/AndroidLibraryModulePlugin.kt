package module

import com.android.build.api.dsl.LibraryExtension
import dsl.alias
import dsl.libs
import dsl.plugin
import dsl.plugins
import dsl.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

open class AndroidLibraryModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                alias(libs.plugin("androidLibrary"))
            }

            configureBasic()
            configureAndroid()

            androidLibrary {
                defaultConfig {
                    compileSdk = libs.version("app-compileSdk").toInt()
                }
                buildTypes {
                    release {
                        isMinifyEnabled = false
                    }
                }
            }

            dependencies {
            }
        }
    }
}

internal fun Project.androidLibrary(action: LibraryExtension.() -> Unit) {
    extensions.configure(action)
}
