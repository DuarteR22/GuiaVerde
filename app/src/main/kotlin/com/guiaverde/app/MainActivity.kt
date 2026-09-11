package com.guiaverde.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.guiaverde.app.ui.GuiaVerdeApp

/**
 * Única Activity da app (padrão em apps Compose: uma Activity, várias telas
 * navegadas dentro dela). O trabalho real de UI está em ui/GuiaVerdeApp.kt.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuiaVerdeApp()
        }
    }
}
