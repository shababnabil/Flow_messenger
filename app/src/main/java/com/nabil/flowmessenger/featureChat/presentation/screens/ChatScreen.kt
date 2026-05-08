package com.nabil.flowmessenger.featureChat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.nabil.flowmessenger.core.domain.models.UserData
import com.nabil.flowmessenger.featureChat.domain.models.MessageItem
import com.nabil.flowmessenger.featureChat.presentation.viewModels.ChatViewModel
import com.nabil.flowmessenger.ui.theme.AppBackgroundColor
import com.nabil.flowmessenger.ui.theme.AppDarkSurfaceColor
import com.nabil.flowmessenger.ui.theme.AppPrimaryColor
import com.nabil.flowmessenger.ui.theme.AppSurfaceColor
import com.nabil.flowmessenger.ui.theme.AppTextPrimaryColor
import com.nabil.flowmessenger.ui.theme.AppTextSecondaryColor
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(
    user: UserData,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        chatViewModel.selectUser(user)
    }

    val messages by chatViewModel.messageList.collectAsState()

    var inputText = chatViewModel.inputText.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        val visibleItemIndex = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index?:0
        if (visibleItemIndex in 1..<2){
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackgroundColor)
            .imePadding()
    ) {
        ChatToolbar(
            userName = user.userName ?: "",
            photoUrl = user.userProfilePicUri,
            onBack = onBack
        )

        HorizontalDivider(
            color = AppDarkSurfaceColor,
            thickness = 1.dp
        )


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            reverseLayout = true
        ) {
            items(messages.reversed(), key = { it.messageId }) { message ->
                ChatBubble(message = message)
            }
        }

        HorizontalDivider(
            color = AppDarkSurfaceColor,
            thickness = 1.dp
        )

        ChatInputBar(
            inputText = inputText.value,
            onInputChange = { chatViewModel.setInputText(it)  },
            onSend = {
                val trimmedText = it.trim()
                if (trimmedText.isNotBlank()){
                    chatViewModel.sendMessage(it)
                    chatViewModel.setInputText("")
                }
            }
        )
    }
}


@Composable
private fun ChatToolbar(
    userName: String,
    photoUrl: String?,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = AppTextPrimaryColor
            )
        }

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(AppSurfaceColor)
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = userName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = userName,
            color = AppTextPrimaryColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
private fun ChatInputBar(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Message…") },
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = AppSurfaceColor,
                unfocusedContainerColor = AppSurfaceColor
            ),
            textStyle = TextStyle(
                color = AppTextPrimaryColor,
                fontSize = 15.sp
            ),
            maxLines = 5
        )

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = { onSend(inputText) },
            modifier = Modifier
                .size(46.dp)
                .background(
                    if (inputText.isNotBlank()) AppPrimaryColor else AppDarkSurfaceColor,
                    CircleShape
                ),
            enabled = inputText.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = if (inputText.isNotBlank()) Color.White else AppTextSecondaryColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun ChatBubble(message: MessageItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (message.isMine) AppPrimaryColor else AppSurfaceColor,
                    shape = RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (message.isMine) 18.dp else 4.dp,
                        bottomEnd = if (message.isMine) 4.dp else 18.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.message,
                color = if (message.isMine) Color.White else AppTextPrimaryColor,
                fontSize = 15.sp,
                lineHeight = 21.sp
            )
        }
    }
}