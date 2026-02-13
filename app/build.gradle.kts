plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.diffplug.spotless)
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint().editorConfigOverride(mapOf("max_line_length" to "100"))
    }
}

android {
    namespace = "com.rasel.calculator"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rasel.calculator"
        minSdk = 26
        targetSdk = 36
        versionCode = 51
        versionName = "51.0"
        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner = "com.rasel.calculator.CustomTestRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.gms.code.scanner)

    //New Material Design
    implementation(libs.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.recyclerview)
}
