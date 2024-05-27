plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.amf.amflix"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.amf.amflix"
        minSdk = 29
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    val retrofitVersion = "2.9.0"
    val okhttpVersion = "4.11.0"
    val coilVersion = "2.4.0"

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.github.chrisbanes:PhotoView:2.3.0")
    kapt("com.github.bumptech.glide:glide:4.13.0")
    implementation("jp.wasabeef:glide-transformations:4.3.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("androidx.constraintlayout:constraintlayout-core:1.0.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    //Android RX
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    // YT
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.0.1")

    implementation ("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-compiler:2.48")

    // Retrofit y GSON para solicitudes HTTP
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion") {
        exclude(module = "okhttp")
    }
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // okhttp logging interceptor
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    // coil Carga de imagenes
    implementation("io.coil-kt:coil:$coilVersion")

    // animaciones
    implementation("com.airbnb.android:lottie:3.4.0")
}