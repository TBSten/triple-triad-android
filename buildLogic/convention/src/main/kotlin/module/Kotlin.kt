package module

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions.optIn.add("kotlin.uuid.ExperimentalUuidApi")
    }
}
