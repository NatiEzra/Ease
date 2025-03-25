import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    id("androidx.navigation.safeargs.kotlin")
    id ("kotlin-kapt")


}
val room_version = "2.6.1"
val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretsProperties =  Properties()
secretsProperties.load( FileInputStream(secretsPropertiesFile))
android {
    namespace = "com.example.ease"
    compileSdk = 35
    buildFeatures {
        buildConfig = true
    }




    defaultConfig {
        applicationId = "com.example.ease"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLOUD_NAME", "\"${project.properties["CLOUD_NAME"] ?: ""}\"")
        buildConfigField("String", "API_KEY", "\"${project.properties["API_KEY"] ?: ""}\"")
        buildConfigField("String", "API_SECRET", "\"${project.properties["API_SECRET"] ?: ""}\"")
        buildConfigField("String", "GOOGLE_CLIENT_ID", secretsProperties["GOOGLE_CLIENT_ID"] as String)
        buildConfigField("String", "Articles_Api", "\"${project.properties["Articles_Api"] ?: ""}\"")


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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.room.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth.ktx)
    implementation (libs.cloudinary.android)
    implementation (libs.picasso)
    implementation(libs.wasabeef.picasso.transformations)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.cardview)
    implementation (libs.okhttp)
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.androidx.navigation.fragment.ktx.v276)
    implementation (libs.androidx.navigation.ui.ktx.v276)
    implementation (libs.androidx.navigation.fragment.ktx.v260)
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)





}