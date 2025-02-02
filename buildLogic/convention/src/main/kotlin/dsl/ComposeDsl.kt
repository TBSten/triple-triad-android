package dsl

import org.gradle.api.Project
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.composeCompiler(actions: ComposeCompilerGradlePluginExtension.() -> Unit) =
    extensions.configure(
        ComposeCompilerGradlePluginExtension::class.java,
        actions,
    )
