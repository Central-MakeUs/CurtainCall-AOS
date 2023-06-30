// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application").version("8.0.1") apply false
    id("com.android.library").version("8.0.1") apply false
    id("org.jetbrains.kotlin.android").version("1.8.10") apply false
    id("org.jetbrains.kotlin.jvm").version("1.8.10") apply false
    id("org.jlleitschuh.gradle.ktlint").version("11.4.2") apply false
    id("io.gitlab.arturbosch.detekt").version("1.23.0") apply true
}

allprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("io.gitlab.arturbosch.detekt")
    }

    afterEvaluate {
        project.apply("$rootDir/gradle/common.gradle")

        detekt {
            buildUponDefaultConfig = true
            config.setFrom(files("$rootDir/detekt-config.yml"))
        }
    }
}
