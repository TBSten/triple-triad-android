plugins {
    alias(libs.plugins.buildLogicModuleFeature)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.ui.feature.game"
}

dependencies {
}
