// Ficheiro raiz: não configura nada diretamente, só declara quais plugins
// o subprojeto (app) tem disponíveis, sem os aplicar aqui (apply false).
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.composeCompiler) apply false
}
