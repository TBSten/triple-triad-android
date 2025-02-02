package dsl

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.detekt(actions: DetektExtension.() -> Unit) = extensions.configure(actions)
