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
    implementation(projects.ui.testing.annotation)
    implementation(platform(libs.composeBom))
    implementation(libs.kotlinxCoroutinesTest)
    implementation(libs.androidxEspressoCore)
    implementation(libs.composeUiTestJunit4)
    implementation(libs.composeUiTestManifest)
    implementation(libs.roborazziCore)
    implementation(libs.roborazziCompose)
    implementation(libs.roborazziJunit)
    implementation(libs.roborazziComposePreviewScannerSupport)
    implementation(libs.composePreviewScanner)
}
