package com.nabil.flowmessenger.featureAuth.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.nabil.flowmessenger.featureAuth.presentation.screens.LoginScreen
import com.nabil.flowmessenger.featureChat.presentation.MainActivity
import com.nabil.flowmessenger.ui.theme.Flow_messengerTheme

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.White.toArgb(),
                Color.White.toArgb()
            )
        )

        setContent {

            Flow_messengerTheme {

                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding)){
                        openChatActivity()
                    }
                }

            }

        }
    }

    private fun openChatActivity(){
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

}