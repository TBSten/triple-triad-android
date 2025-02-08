plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveHilt)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.domain"
}

dependencies {
    api(projects.domain.error)
    api(libs.tartCore)
    api(libs.arrowKtOptics)
    ksp(libs.arrowKtOpticsKspPlugin)
}
