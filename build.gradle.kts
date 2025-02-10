// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.secret.gradle.plugin) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
}

buildscript {
    dependencies {
        classpath(libs.mapsplatform)
    }
}