package primitive

import dsl.alias
import dsl.androidTestImplementation
import dsl.composeCompiler
import dsl.debugImplementation
import dsl.implementation
import dsl.library
import dsl.libs
import dsl.plugin
import dsl.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies

open class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            @Suppress("DEPRECATION")
            setUpCompose()

            plugins {
                apply(RoborazziPlugin::class)
            }
        }
    }
}

@Deprecated(
    message = "Don't use setUpCompose() directly. Please use `buildLogic.primitive.compose`.",
    replaceWith = ReplaceWith("plugins {\n    alias(libs.buildLogicPrimitiveCompose)\n}\n"),
)
fun Project.setUpCompose() {
    plugins {
        alias(libs.plugin("kotlinCompose"))
    }

    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("compose_stability_config.conf"))
    }

    dependencies {
        implementation(platform(libs.library("composeBom")))
        implementation(libs.library("composeUi"))
        implementation(libs.library("composeUiGraphics"))
        implementation(libs.library("composeUiToolingPreview"))
        implementation(libs.library("composeMaterial3"))

        androidTestImplementation(platform(libs.library("composeBom")))
        androidTestImplementation(libs.library("composeUiTestJunit4"))

        debugImplementation(libs.library("composeUiTooling"))
        debugImplementation(libs.library("composeUiTestManifest"))

        "lintChecks"(libs.library("slackComposeLint"))
    }
}
