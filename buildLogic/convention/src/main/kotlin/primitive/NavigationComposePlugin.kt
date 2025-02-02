package primitive

import dsl.implementation
import dsl.library
import dsl.libs
import dsl.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

open class NavigationComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(KotlinxSerializationPlugin::class)
            }

            dependencies {
                implementation(libs.library("navigationCompose"))
            }
        }
    }
}
