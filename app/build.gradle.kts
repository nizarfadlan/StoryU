plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.devToolsKsp)
    id("androidx.navigation.safeargs")
    alias(libs.plugins.gmapsSecrets)
}

android {
    namespace = "com.nizarfadlan.storyu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nizarfadlan.storyu"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
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
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    testOptions {
        animationsDisabled = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    // UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.glide)
    implementation(libs.androidx.activity.activity.ktx3)
    implementation(libs.androidx.fragment.fragment.ktx)
    implementation(libs.circleimageview)
    implementation(libs.androidx.swiperefreshlayout)

    // Map
    implementation(libs.play.services.maps)

    // Fused
    implementation(libs.play.services.location)

    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Paging
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.room.paging)

    // Retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //  Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Coroutines
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Image
    implementation(libs.androidx.exifinterface)

    /* Testing */
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.junit)
    
    // IdlingResource
    implementation(libs.androidx.espresso.idling.resource)

    // special instrumentation testing
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)

    // Fragment
    debugImplementation(libs.androidx.fragment.testing)
    debugImplementation(libs.androidx.fragment.testing.manifest)

    // Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    // MockWebServer
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp.tls)

    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    implementation(libs.timber)
}