package com.duoc.galpones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.galpones.ui.navigation.AppNavHost
import com.duoc.galpones.ui.theme.MonitoreoGalponesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonitoreoGalponesTheme {
                AppNavHost()
            }
        }
    }
}
