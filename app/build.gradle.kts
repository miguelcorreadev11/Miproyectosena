// Módulo app de ArchiveX Móvil (AA2-EV02): cliente Android de la API REST de ArchiveX.
plugins {
    id("com.android.application")
}

android {
    namespace = "com.archivex.movil"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.archivex.movil"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        // Las pruebas unitarias locales devuelven valores por defecto en las clases de Android.
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    // ---- Capa de presentación ----
    implementation("androidx.appcompat:appcompat:1.7.0")            // AppCompatActivity, Toolbar
    implementation("com.google.android.material:material:1.12.0")   // Material Design
    implementation("androidx.recyclerview:recyclerview:1.3.2")       // Listas eficientes
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") // Deslizar para actualizar

    // ---- Pruebas unitarias (JVM local) ----
    testImplementation("junit:junit:4.13.2")
    // Implementación real de org.json para las pruebas (en Android viene incluida).
    testImplementation("org.json:json:20240303")
}
