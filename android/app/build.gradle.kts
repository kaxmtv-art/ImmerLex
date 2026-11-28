plugins { id("com.android.application") }
android {
    namespace = "kz.zhetysu.immerlex"
    compileSdk = 36
    defaultConfig {
        applicationId = "kz.zhetysu.immerlex"
        minSdk = 24
        targetSdk = 36
        versionCode = (project.findProperty("vc") as String? ?: "1").toInt()
        versionName = (project.findProperty("vn") as String? ?: "1.0")
    }
    buildTypes { release { isMinifyEnabled = false } }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
dependencies {
    implementation("androidx.webkit:webkit:1.12.1")
    implementation("androidx.core:core:1.13.1")
}
