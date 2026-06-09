plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    // kapt es el procesador de anotaciones de Kotlin
    // Room lo necesita para generar código a partir de las anotaciones
    id("kotlin-kapt")
}

android {
    namespace = "com.ejemplo.misfinanzas"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ejemplo.misfinanzas"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.fragment:fragment-ktx:1.8.8")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter Gson: convierte JSON a objetos Kotlin automáticamente
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Gson: librería de Google para parsear JSON
    implementation("com.google.code.gson:gson:2.10.1")
    // Coroutines Android: para ejecutar llamadas de red en segundo plano
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}