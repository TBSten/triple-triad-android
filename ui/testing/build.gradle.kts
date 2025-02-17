import primitive.setUpCompose

plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
}

// buildLogic.primitive.compose を apply してしまうと 循環参照になってしまうため、直接 setUpCompose を呼び出す。
@Suppress("DEPRECATION")
setUpCompose()

android {
    namespace = "${libs.versions.app.applicationId.get()}.ui.testing"
}

dependencies {
    api(projects.ui.testing.annotation)
    api(platform(libs.composeBom))
    api(libs.composeUiTooling)
    api(libs.kotlinxCoroutinesTest)
    api(libs.androidxEspressoCore)
    api(libs.composeUiTestJunit4)
    api(libs.composeUiTestManifest)
    api(libs.roborazziCore)
    api(libs.roborazziCompose)
    api(libs.roborazziJunit)
    api(libs.roborazziComposePreviewScannerSupport)
    api(libs.composePreviewScanner)
}
