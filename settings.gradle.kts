pluginManagement {
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

rootProject.name = "OmdbCompose"
include(":app")

include(":features:details:data")
include(":features:details:domain")
include(":features:details:presentation")

include(":features:search:data")
include(":features:search:domain")
include(":features:search:presentation")

include(":core:framework")


include("features:shared:network:data")
include("features:shared:presentation:resources")
include("features:shared:presentation:ui")
