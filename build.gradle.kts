// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0-alpha08" apply false
    id("com.android.library") version "8.1.0-alpha08" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}
buildscript {
    dependencies {
        classpath ("io.realm:realm-gradle-plugin:10.15.1")
    }
}