package com.nabil.flowmessenger.featureAuth.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.nabil.flowmessenger.R
import com.nabil.flowmessenger.featureAuth.presentation.models.LoginStateType
import com.nabil.flowmessenger.featureAuth.presentation.viewmodels.LoginViewModel
import com.nabil.flowmessenger.ui.theme.AppAccentColor
import com.nabil.flowmessenger.ui.theme.AppBackgroundColor
import com.nabil.flowmessenger.ui.theme.AppDarkSurfaceColor
import com.nabil.flowmessenger.ui.theme.AppPrimaryColor
import com.nabil.flowmessenger.ui.theme.AppSecondaryColor
import com.nabil.flowmessenger.ui.theme.AppSurfaceColor
import com.nabil.flowmessenger.ui.theme.AppTextPrimaryColor
import com.nabil.flowmessenger.ui.theme.AppTextSecondaryColor

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onLoggedIn : ()-> Unit
) {
    val loginState by loginViewModel.loginState.collectAsState()
    val errorMessage = loginState.errorMessage
    val context = LocalContext.current

    //LaunchedEffect(Unit) { loginViewModel.checkForLoginSession() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 6 }
        ) {
            when (loginState.loginStateType) {

                LoginStateType.LOGGED_IN -> {
                    onLoggedIn()
                }

                else -> {
                    LoggedOutSection(
                        isLoading = loginState.loginStateType == LoginStateType.LOADING,
                        onLoginButtonClicked = { loginViewModel.logIn() }
                    )
                    if (loginState.loginStateType == LoginStateType.ERROR) {
                        Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun LoggedOutSection(
    isLoading: Boolean,
    onLoginButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(AppAccentColor.copy(alpha = 0.45f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.chat_bubble_24px),
                contentDescription = null,
                tint = AppPrimaryColor,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "Realtime Chat",
            color = AppTextPrimaryColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Clean. Minimal. Instant.",
            color = AppTextSecondaryColor,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(52.dp))

        Button(
            onClick = onLoginButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppPrimaryColor,
                contentColor = Color.White
            ),
            elevation = null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {


                GoogleGMark()
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Continue with Google",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                if (isLoading) {
                    Spacer(Modifier.width(12.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(17.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }else{
                    Spacer(modifier = Modifier.width(29.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = AppDarkSurfaceColor,
                thickness = 1.dp
            )
            Text(
                text = "  Secure login  ",
                color = AppTextSecondaryColor,
                fontSize = 12.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = AppDarkSurfaceColor,
                thickness = 1.dp
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "By continuing, you agree to our\nTerms of Service and Privacy Policy.",
            color = AppTextSecondaryColor.copy(alpha = 0.55f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}


@Composable
private fun GoogleGMark() {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(14.dp),
            painter = painterResource(R.drawable.google_icon),
            contentDescription = "Login with Google"
        )
    }
}