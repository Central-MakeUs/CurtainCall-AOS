package com.cmc.curtaincall.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.JavaVersion
import org.gradle.api.Project

private fun CommonExtension<*, *, *, *>.configureCommon() {
    compileSdk = 34
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

internal fun Project.configureKotlinAndroidApplication(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        configureCommon()
        configureKotlin()
        defaultConfig {
            minSdk = 26
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            buildConfigField("String", "GET_STREAM_API_KEY", gradleLocalProperties(rootDir).getProperty("GET_STREAM_API_KEY"))
            buildConfigField("String", "KAKAO_APP_KEY", gradleLocalProperties(rootDir).getProperty("KAKAO_APP_KEY"))
            buildConfigField("String", "NAVER_CLIENT_ID", gradleLocalProperties(rootDir).getProperty("NAVER_CLIENT_ID"))
            buildConfigField("String", "NAVER_CLIENT_SECRET", gradleLocalProperties(rootDir).getProperty("NAVER_CLIENT_SECRET"))
            resValue("string", "NAVER_API_MAP_KEY", gradleLocalProperties(rootDir).getProperty("NAVER_API_MAP_KEY"))
            vectorDrawables {
                useSupportLibrary = true
            }
        }
    }
}

internal fun Project.configureKotlinAndroidLibrary(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        configureCommon()
        configureKotlin()
        defaultConfig {
            minSdk = 26
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}
