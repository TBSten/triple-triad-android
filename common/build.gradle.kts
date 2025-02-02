plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.common"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.timber)
}
