import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.buildLogicModuleFeature)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.ui.feature.game"
}

dependencies {
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        optIn.add("androidx.compose.animation.ExperimentalSharedTransitionApi")
    }
}
