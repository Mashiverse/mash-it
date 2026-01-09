import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "io.mashit.mashit"
    compileSdk = 36 // AGP 8+ assignment syntax

    defaultConfig {
        applicationId = "io.mashit.mashit"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Essential for large Web3 libraries (Web3j/Reown)
        multiDexEnabled = true

        val localProperties = Properties()
        val localPropertiesFile = File(rootDir, "keys.properties")
        if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
            localPropertiesFile.inputStream().use {
                localProperties.load(it)
            }
        }

        buildConfigField("String", "REOWN_PROJECT_ID", localProperties.getProperty("REOWN_PROJECT_ID") ?: "\"\"")
        buildConfigField("String", "ALCHEMY_API_KEY", localProperties.getProperty("ALCHEMY_API_KEY") ?: "\"\"")
        buildConfigField("String", "MASH_IT_API_KEY", localProperties.getProperty("MASH_IT_API_KEY") ?: "\"\"")
    }

    packaging {
        resources {
            // FIX for Jackson-core and AWS SDK metadata conflicts
            // Using wildcards to catch both -LICENSE and -NOTICE and any other future ones
            pickFirsts += "META-INF/FastDoubleParser-*"

            // FIX for duplicate Netty/Vert.x and networking files
            pickFirsts += "META-INF/INDEX.LIST"
            pickFirsts += "META-INF/io.netty.versions.properties"

            // Common Web3j / Jackson / OkHttp metadata conflicts
            pickFirsts += "META-INF/LICENSE"
            pickFirsts += "META-INF/NOTICE"
            pickFirsts += "META-INF/okio.kotlin_module"
            pickFirsts += "META-INF/kotlinx-serialization-json.kotlin_module"
            pickFirsts += "META-INF/gradle/incremental.annotation.processors"

            // Exclude non-essential metadata files to keep APK clean
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "/META-INF/AL2.0"
            excludes += "/META-INF/LGPL2.1"
            excludes += "META-INF/native-image/**"
        }
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
        // FIX for "Record desugaring" / "global synthetic" error
        // Allows Java 14+ Records (used in Web3j) to work on Android
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // DESUGARING: Bridge between Java 17 features and older Android runtimes
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")

    // Android / Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.remote.creation.core)
    implementation(libs.litert.api)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.navigation.material)

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Web3 & Reown
    implementation(platform(libs.android.bom))
    implementation(libs.android.core)
    implementation(libs.appkit)
    implementation(libs.sign)
    implementation(libs.coinbase.wallet.sdk)

    // Web3j Core
    implementation(libs.core)

    // Coinbase
    implementation(libs.coinbase.wallet.sdk)

    // Networking
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Image Loading (Coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)

    // Icons
    implementation(libs.androidx.compose.material.icons.extended)
}