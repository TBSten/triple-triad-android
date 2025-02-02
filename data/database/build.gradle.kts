plugins {
    alias(libs.plugins.buildLogicModuleAndroidLibrary)
    alias(libs.plugins.buildLogicPrimitiveRoom)
    alias(libs.plugins.buildLogicPrimitiveHilt)
}

android {
    namespace = "${libs.versions.app.applicationId.get()}.data.database"
}

dependencies {
    api(libs.roomRuntime)
    api(libs.roomKtx)
}
