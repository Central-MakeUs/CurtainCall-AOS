pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
        maven(url = "https://repository.map.naver.com/archive/maven")
    }
}
rootProject.name = "CurtainCall"
include(":app")
include(":data")
include(":core:base")
include(":core:network")
include(":common:designsystem")
include(":common:utility")
include(":domain")
include(":feature:home")
include(":feature:show")
include(":feature:livetalk")
include(":feature:partymember")
include(":feature:mypage")
include(":feature:auth")
include(":core:local")
include(":design")
include(":common:navigation")
