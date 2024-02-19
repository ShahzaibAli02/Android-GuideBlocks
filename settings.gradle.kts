import java.net.URI

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven ("https://jitpack.io")
        maven ("https://repo.contextu.al/sdk/contextual/" )
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ("https://jitpack.io")
        maven ("https://repo.contextu.al/sdk/contextual/" )
    }
}

rootProject.name = "Android-GuideBlocks"
include(":app")
 