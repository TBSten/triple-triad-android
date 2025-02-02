import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveHilt)
    alias(libs.plugins.buildLogicPrimitiveOpenApi)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.data.api"
}

dependencies {
    api(libs.retrofit)
}

tasks.named<GenerateTask>("openApiGenerate") {
    packageName = "${android.namespace}.generated"
}
