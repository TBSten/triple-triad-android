pluginManagement {
    includeBuild("./buildLogic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "TripleTriad"
include(":common")
include(":common:testing")
include(":tools:debug")
include(":app")
include(":data")
include(":data:api")
include(":data:database")
include(":data:preferences")
include(":domain")
include(":domain:error")
include(":ui")
include(":ui:designSystem")
include(":ui:navigation")
include(":ui:testing")
include(":ui:testing:annotation")
include(":ui:feature:example")
