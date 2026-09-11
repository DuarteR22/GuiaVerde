plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    // Em vez de jvmToolchain(17) — que obriga o Gradle a ENCONTRAR/descarregar
    // um JDK 17 instalado à parte — só pedimos ao compilador Kotlin para
    // gerar bytecode alvo 17. Isto corre perfeitamente com o JDK do próprio
    // Android Studio (JBR), seja qual for a sua versão.
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

android {
    namespace = "com.guiaverde.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.guiaverde.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // BOM (Bill of Materials): fixa, numa versão só, as versões de TODAS as
    // bibliotecas androidx.compose.* usadas abaixo — evitas ter de indicar
    // (e alinhar manualmente) uma versão para cada uma.
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    // O pacote "core" de ícones do Compose só traz ~30 ícones comuns. A
    // "extended" traz o catálogo inteiro do Material Symbols (milhares),
    // incluindo os que o design usa (trip_origin, swap_vert, my_location...).
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // `collectAsStateWithLifecycle()` — como `collectAsState()`, mas pausa a
    // recolha quando o ecrã não está visível (STOPPED), em vez de continuar
    // a recompor em segundo plano. Google recomenda-a como omissão para
    // observar StateFlow de um ViewModel a partir da UI.
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    // Cliente HTTP mínimo para o autocompletar de Origem/Destino (Passo 12):
    // Retrofit descreve o endpoint do Photon como uma interface Kotlin
    // (`PhotonApi`), o converter-gson faz o parse do GeoJSON devolvido para
    // as data classes de `data/remote`. Sem mais nada por cima (sem
    // Hilt/Dagger, sem CallAdapter à parte) — Retrofit já suporta `suspend
    // fun` nativamente desde a versão 2.6.
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    // Só usadas em debug builds: preview no Android Studio e inspeção de UI.
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
}
