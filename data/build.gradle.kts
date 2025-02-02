plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveHilt)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.data"
}

dependencies {
    implementation(projects.data.preferences)
    implementation(projects.data.database)
    implementation(projects.data.api)
    implementation(projects.domain)
}
