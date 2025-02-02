import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "build.logic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // TODO compileOnly に replace
    compileOnly(libs.kotlinGradlePlugin)
    compileOnly(libs.kotlinGradlePluginApi)

    compileOnly(libs.androidGradlePlugin)
    compileOnly(libs.composeGradlePlugin)
    compileOnly(libs.ktlintGradlePlugin)
    compileOnly(libs.detektGradlePlugin)
    compileOnly(libs.roborazziGradlePlugin)
    compileOnly(libs.roomGradlePlugin)
    compileOnly(libs.openApiGradlePlugin)
}

gradlePlugin {
    plugins {
        register("android.application") {
            id = "buildLogic.module.android.application"
            implementationClass = "module.AndroidApplicationModulePlugin"
        }
        register("android.library") {
            id = "buildLogic.module.android.library"
            implementationClass = "module.AndroidLibraryModulePlugin"
        }
        register("feature") {
            id = "buildLogic.module.feature"
            implementationClass = "module.FeatureModulePlugin"
        }

        register("lint") {
            id = "buildLogic.primitive.lint"
            implementationClass = "primitive.LintPlugin"
        }
        register("compose") {
            id = "buildLogic.primitive.compose"
            implementationClass = "primitive.ComposePlugin"
        }
        register("navigation.compose") {
            id = "buildLogic.primitive.navigation.compose"
            implementationClass = "primitive.NavigationComposePlugin"
        }
        register("kotlinx.serialization") {
            id = "buildLogic.primitive.kotlinx.serialization"
            implementationClass = "primitive.KotlinxSerializationPlugin"
        }
        register("hilt") {
            id = "buildLogic.primitive.hilt"
            implementationClass = "primitive.HiltPlugin"
        }
        register("room") {
            id = "buildLogic.primitive.room"
            implementationClass = "primitive.RoomPlugin"
        }
        register("roborazzi") {
            id = "buildLogic.primitive.roborazzi"
            implementationClass = "primitive.RoborazziPlugin"
        }
        register("open.api") {
            id = "buildLogic.primitive.open.api"
            implementationClass = "primitive.OpenApiPlugin"
        }
    }
}
