plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.netflix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.netflix"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation (libs.appcompat)
    implementation (libs.androidx.recyclerview)
    implementation (libs.material)
    implementation(libs.squareup.retrofit)  // Retrofit
    implementation(libs.androidx.lifecycle.extensions)  // Activity for lifecycle management (optional)
    implementation(libs.androidx.room.runtime.v251)  // Room
    implementation(libs.room.compiler)  // Room compiler for annotations (לא annotationProcessor)
    implementation(libs.play.services.fitness.v2101)  // Play Services Fitness
    implementation(libs.androidx.ui)  // UI graphics AndroidX
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    annotationProcessor (libs.androidx.room.compiler.v250)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.converter.gson)  // Gson converte // r for Retrofit
    implementation (libs.java.jwt)
    implementation (libs.glide)
    //annotationProcessor (libs.compiler)



}
configurations.all {
    resolutionStrategy {
        force("org.jetbrains:annotations:23.0.0")
        exclude (module = ("annotations"))
    }
}