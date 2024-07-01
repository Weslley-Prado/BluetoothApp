plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}



android {
    compileSdk = 34
    namespace = "com.wrprado.bluetoothapp"
    defaultConfig {
        applicationId = "com.wrprado.bluetoothapp"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildToolsVersion = "34.0.0"
    compileOptions {

        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("com.polidea.rxandroidble2:rxandroidble:1.11.1")
    implementation ("io.reactivex.rxjava2:rxandroid:2.0.1")

}
