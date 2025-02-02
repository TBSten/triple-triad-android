plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.testing"
}

dependencies {
    api(libs.kotlinxCoroutinesTest)
    api(libs.junit)
    api(libs.kotlinTestJunit)
    api(libs.mockk)
}
