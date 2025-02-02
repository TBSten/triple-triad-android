plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.data.preferences"
}

dependencies {
    implementation(libs.datastorePreferences)
}
