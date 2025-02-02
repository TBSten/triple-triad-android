import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.writeText

plugins {
    // build-logic primitive plugins
    alias(libs.plugins.buildLogicPrimitiveLint) apply false
    alias(libs.plugins.buildLogicPrimitiveCompose) apply false
    alias(libs.plugins.buildLogicPrimitiveNavigationCompose) apply false
    alias(libs.plugins.buildLogicPrimitiveKotlinxSerialization) apply false
    alias(libs.plugins.buildLogicPrimitiveHilt) apply false
    alias(libs.plugins.buildLogicPrimitiveRoom) apply false
    alias(libs.plugins.buildLogicPrimitiveOpenApi) apply false

    // build-logic module plugins
    alias(libs.plugins.buildLogicModuleAndroidApplication) apply false
    alias(libs.plugins.buildLogicModuleAndroidLibrary) apply false
    alias(libs.plugins.buildLogicModuleFeature) apply false

    // other plugins
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinCompose) apply false
    alias(libs.plugins.kotlinPluginSerialization) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.openApi) apply false
}

/**
 * usages
 *
 * ```shell
 * ./gradlew changeApplicationId -PnewApplicationId=new.packageName
 * # or
 * ./gradlew changeApplicationId -PoldApplicationId=old.packageName -PnewApplicationId=new.packageName
 * ```
 */
val changeApplicationId by tasks.creating {

    val newApplicationIdProperty = providers.gradleProperty("newApplicationId")
    val oldApplicationIdProperty = providers.gradleProperty("oldApplicationId")

    // replace libs.versions.toml app-applicationId
    doLast {
        val newApplicationId = newApplicationIdProperty.get()
        val oldApplicationId = oldApplicationIdProperty.orNull ?: "your.projectPackage"

        println("newApplicationId: $newApplicationId")
        println("oldApplicationId: $oldApplicationId")

        val libsVersionsToml = rootProject.file("gradle/libs.versions.toml")
        libsVersionsToml.writeText(
            rootProject.file("gradle/libs.versions.toml")
                .readText()
                .replace(oldApplicationId, newApplicationId),
        )
    }

    // replace directories and replace package name
    doLast {
        val newPackage =
            newApplicationIdProperty.get()
        val newPackagePath = newPackage.replace(".", "/")
        val oldPackage = oldApplicationIdProperty.orNull ?: "your.projectPackage"
        val oldPackagePath = oldPackage.replace(".", "/")
        val oldPackageFiles =
            rootProject.fileTree(".") {
                include("**/$oldPackagePath/**")
            }
        oldPackageFiles.forEach { oldFile ->
            println(oldFile.path)
            println("  > ${oldFile.absolutePath.replace(oldPackagePath, newPackagePath)}")
            val newFile = Paths.get(oldFile.absolutePath.replace(oldPackagePath, newPackagePath))
            newFile.parent.createDirectories()
            Files.move(
                oldFile.toPath(),
                newFile,
            )
            newFile.writeText(
                newFile.readText()
                    .replace(oldPackage, newPackage),
            )
        }
    }
}
