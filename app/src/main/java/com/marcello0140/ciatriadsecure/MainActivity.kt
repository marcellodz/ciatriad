package com.marcello0140.ciatriadsecure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marcello0140.ciatriadsecure.ui.screen.MainScreen
import com.marcello0140.ciatriadsecure.ui.theme.CiaTriadSecureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CiaTriadSecureTheme {
                MainScreen() // ✅ Kirim context ke MainScreen
            }
        }
    }
}
