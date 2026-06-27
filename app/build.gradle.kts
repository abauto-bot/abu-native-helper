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
        versionCode = 9
        versionName = "0.9.0-v9.9"
    }
}
