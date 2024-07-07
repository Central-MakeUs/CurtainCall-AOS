import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.curtaincall.android.library)
    alias(libs.plugins.curtaincall.android.hilt)
}

android {
    namespace = "com.cmc.curtaincall.core.network"

    defaultConfig {
        buildConfigField("String", "CURTAIN_CALL_BASE_URL", gradleLocalProperties(rootDir).getProperty("CURTAIN_CALL_BASE_URL"))
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:local"))

    // logging
    implementation(libs.timber)

    implementation(libs.bundles.retrofit)

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
}
