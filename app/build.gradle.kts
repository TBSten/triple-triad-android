plugins {
    alias(libs.plugins.buildLogicModuleAndroidApplication)
    alias(libs.plugins.buildLogicPrimitiveNavigationCompose)
}

android {
    /*
    roborazzi Compose Preview Support で他モジュールのファイルが収集されないようにするために
    namespace の suffix として app を使用している。
     */
    namespace = "${libs.versions.app.applicationId.get()}.app"

    defaultConfig {
        applicationId =
            libs.versions.app.applicationId
                .get()
        versionCode =
            libs.versions.app.versionCode
                .get()
                .toInt()
        versionName =
            libs.versions.app.versionName
                .get()
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.ui)
    implementation(projects.tools.debug)

    // feature modules
    implementation(projects.ui.feature.example)
    implementation(projects.ui.feature.game)

    implementation(libs.andridxCoreSplashScreen)
}
