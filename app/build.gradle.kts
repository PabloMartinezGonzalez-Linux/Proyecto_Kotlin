plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    // Plugin para Kotlin Serialization
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

// Aplica el plugin de Hilt de forma clásica
apply(plugin = "dagger.hilt.android.plugin")

android {
    namespace = "com.example.login"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.login"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Firebase
    implementation("com.google.firebase:firebase-auth")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")

    // JSON Parsing
    implementation("com.google.code.gson:gson:2.10")

    // Lifecycle - LiveData & ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.cardview)

    // Hilt - Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Retrofit y OkHttp para conectarse al backend
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.fragment:fragment-ktx:1.5.7")
}

// Configuración de kapt
kapt {
    correctErrorTypes = true

    javacOptions {
        option("--add-exports", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED")
    }
}
