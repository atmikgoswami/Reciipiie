import java.io.FileInputStream
import java.util.Properties


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.reciipiie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.reciipiie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val properties = Properties()
        properties.load(FileInputStream(rootProject.file("local.properties")))
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
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
    }
    buildFeatures {
        android.buildFeatures.buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Compose ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    //Network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //JSON to Kotlin object mapping
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Image Loading
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.android.gms:play-services-auth:21.1.1")

    val nav_version = "2.7.5"
    val compose_version = "1.6.0-alpha08"
    val room = "2.6.0"

    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")

    implementation ("org.jsoup:jsoup:1.15.3")

    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.0.2")

    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.0.2")

    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    implementation("androidx.compose.runtime:runtime-livedata:1.5.2")
    implementation("androidx.compose.material:material-icons-extended")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}