@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.curtaincall.android.application)
    alias(libs.plugins.curtaincall.android.application.compose)
    alias(libs.plugins.curtaincall.android.hilt)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.cmc.curtaincall"

    defaultConfig {
        applicationId = "com.cmc.curtaincall"
        versionCode = 11
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":common:designsystem"))
    implementation(project(":common:navigation"))
    implementation(project(":common:utility"))
    implementation(project(":core:base"))
    implementation(project(":core:network"))
    implementation(project(":core:local"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:partymember"))

    // androidx
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.splashscreen)

    // compose
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.getstream.chat.android.compose)

    // logging
    implementation(libs.timber)

    // auth
    implementation(libs.kakao.user)
    implementation(libs.naver.login)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    // test
    debugImplementation(libs.androidx.compose.ui.tooling)
}
