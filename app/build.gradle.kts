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



    // Dependencia para pruebas unitarias con JUnit
    testImplementation("junit:junit:4.13.2")

    // Dependencias para mockear Firebase con Mockito
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito:mockito-inline:4.8.0")

    // Firebase para pruebas unitarias
    testImplementation("com.google.firebase:firebase-database:20.2.0")

}

