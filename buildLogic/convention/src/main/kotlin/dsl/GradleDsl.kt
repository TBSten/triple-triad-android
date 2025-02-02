package dsl

import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager

fun Project.plugins(block: PluginManager.() -> Unit) {
    pluginManager.apply(block)
}
