package com.nabil.flowmessenger.featureChat.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nabil.flowmessenger.core.domain.models.UserData
import com.nabil.flowmessenger.featureAuth.presentation.SignInActivity
import com.nabil.flowmessenger.featureChat.presentation.screens.ChatScreen
import com.nabil.flowmessenger.featureChat.presentation.screens.UserListScreen
import com.nabil.flowmessenger.featureChat.presentation.viewModels.ChatViewModel
import com.nabil.flowmessenger.featureChat.presentation.viewModels.UserListViewModel
import com.nabil.flowmessenger.ui.theme.Flow_messengerTheme

class MainActivity : ComponentActivity() {

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

                val userListViewModel: UserListViewModel = viewModel()
                val selectedUserData by userListViewModel.selectedUserData.collectAsState()

                if (!userListViewModel.isLoggedIn()) {
                    openLoginActivity()
                } else {

                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = "userList"
                        ) {

                            composable("userList") {
                                UserListScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    userListViewModel = userListViewModel
                                ) {
                                    userListViewModel.selectUser(it)
                                    navController.navigate("chatScreen")
                                }
                            }

                            composable("chatScreen") {
                                ChatScreen(
                                    user = selectedUserData!!,
                                    { navController.popBackStack() },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }

                        }


                    }

                }

            }
        }
    }

    private fun openLoginActivity() {
        startActivity(Intent(this@MainActivity, SignInActivity::class.java))
        finish()
    }

}