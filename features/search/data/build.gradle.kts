plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.omdb.search.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.kotlinx)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    implementation(project(":features:search:domain"))
    implementation(project(":features:shared:network:data"))
    implementation(project(":core:framework"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation(libs.test.turbine)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.kotlin)
}