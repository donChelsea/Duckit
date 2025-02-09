buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin.v255)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}