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
        versionCode = 12
        versionName = "1.0.9-v10.9"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
