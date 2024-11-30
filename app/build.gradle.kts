plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "es.ucm.fdi.pistaypato"
    compileSdk = 34

    defaultConfig {
        applicationId = "es.ucm.fdi.pistaypato"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)
    implementation(libs.recyclerview)
    implementation(libs.firebase.database)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.recyclerview.v121)
    //he cambiado esto a la ultima version
    implementation(libs.firebase.database.ktx)
    implementation ("com.google.android.material:material:1.9.0")
    implementation("com.google.firebase:firebase-auth:21.1.0")
    //Para email
    implementation("com.sun.mail:android-mail:1.6.2")
    implementation("com.sun.mail:android-activation:1.6.2")
    //para los test
    // Dependencias para pruebas
    testImplementation("junit:junit:4.13.2")

    // Dependencia para Mockk (recomendado para Kotlin)
    testImplementation("io.mockk:mockk:1.13.4")

    // Dependencia para coroutines en pruebas
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Dependencias para Firebase Firestore en pruebas
    testImplementation("com.google.firebase:firebase-firestore-ktx:24.7.1")
}

