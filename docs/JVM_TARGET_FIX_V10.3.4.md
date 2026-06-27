# V10.3.4 JVM Target Fix

GitHub Actions failed because Java and Kotlin compile targets were inconsistent:

- compileDebugJavaWithJavac = 1.8
- compileDebugKotlin = 17

Fix:
- Java sourceCompatibility = VERSION_17
- Java targetCompatibility = VERSION_17
- Kotlin jvmTarget = 17

Expected:
- compileDebugKotlin should pass
- assembleDebug should create debug APK
