import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.curtaincall.android.library.compose)
    alias(libs.plugins.curtaincall.android.hilt)
}

android {
    namespace = "com.cmc.curtaincall.feature.auth"

    defaultConfig {
        resValue("string", "KAKAO_REDIRECT_SCHEME", "kakao${gradleLocalProperties(rootDir).getProperty("KAKAO_APP_KEY")}")
    }
}

dependencies {
    implementation(project(":common:designsystem"))
    implementation(project(":common:navigation"))
    implementation(project(":core:base"))
    implementation(project(":domain"))
    implementation(project(":feature:home"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)

    // compose
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.webview)
    implementation(libs.getstream.chat.android.compose)

    // logging
    implementation(libs.timber)

    // auth
    implementation(libs.kakao.user)
    implementation(libs.naver.login)
}
