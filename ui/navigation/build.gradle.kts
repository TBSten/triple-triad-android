plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveCompose)
    alias(libs.plugins.buildLogicPrimitiveNavigationCompose)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.ui.navigation"
}

dependencies {
}
