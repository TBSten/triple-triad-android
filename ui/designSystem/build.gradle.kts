plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveCompose)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.ui.designSystem"
}

dependencies {
    api(platform(libs.composeBom))
    api(libs.composeFoundation)
    api(libs.composeFoundationLayout)
    api(libs.composeUi)
    api(libs.composeUiTooling)
    api(libs.composeUiToolingPreview)
    api(libs.composeUiUtil)
    api(libs.composeMaterialRipple)
}
