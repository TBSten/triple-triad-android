plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveCompose)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.ui.designSystem"
}

dependencies {
}
