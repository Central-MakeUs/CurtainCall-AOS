@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.curtaincall.android.library.compose)
    alias(libs.plugins.curtaincall.android.hilt)
}

android {
    namespace = "com.cmc.curtaincall.feature.home"
}

dependencies {
    implementation(project(":common:designsystem"))
    implementation(project(":common:navigation"))
    implementation(project(":common:utility"))
    implementation(project(":core:base"))
    implementation(project(":domain"))
    implementation(project(":feature:show"))
    implementation(project(":feature:livetalk"))
    implementation(project(":feature:partymember"))
    implementation(project(":feature:mypage"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)

    // compose
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.coil.compose)

    implementation(libs.getstream.chat.android.compose)

    // logging
    implementation(libs.timber)
}
