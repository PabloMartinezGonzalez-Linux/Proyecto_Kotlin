// build.gradle.kts (archivo raíz del proyecto)

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // Se recomienda incluirlo
    }
    dependencies {
        // Aquí se agrega el plugin de Hilt con las coordenadas correctas.
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

plugins {
    // Estos plugins se declaran para aplicarse en módulos cuando se use "apply false"
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0" apply false // ✅ Agregar aquí
}
