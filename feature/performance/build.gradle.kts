@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.curtaincall.android.library.compose)
    alias(libs.plugins.curtaincall.android.hilt)
}

android {
    namespace = "com.cmc.curtaincall.feature.performance"
}

dependencies {
    implementation(project(":common:designsystem"))
    implementation(project(":common:utility"))
    implementation(project(":core:base"))
    implementation(project(":domain"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)

    // compose
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.naver.map.compose)
    implementation(libs.coil.compose)
    implementation(libs.calendar.compose)

    // logging
    implementation(libs.timber)
}
