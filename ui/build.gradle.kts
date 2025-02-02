plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveCompose)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.ui"
}

dependencies {
    api(projects.ui.navigation)
    api(projects.ui.designSystem)
    implementation(projects.domain)
    implementation(libs.androidxLifecycleRuntimeKtx)
    implementation(libs.androidxLifecycleViewModelKtx)

    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}
