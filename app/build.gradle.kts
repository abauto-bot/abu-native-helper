plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.abuos.nativehelper"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.abuos.nativehelper"
        minSdk = 26
        targetSdk = 35
        versionCode = 11
        versionName = "1.0.8-v10.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
