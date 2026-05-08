package com.nabil.flowmessenger.featureChat.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.nabil.flowmessenger.R
import com.nabil.flowmessenger.core.domain.models.UserData
import com.nabil.flowmessenger.featureChat.presentation.viewModels.UserListViewModel
import com.nabil.flowmessenger.ui.theme.AppBackgroundColor
import com.nabil.flowmessenger.ui.theme.AppDarkSurfaceColor
import com.nabil.flowmessenger.ui.theme.AppSurfaceColor
import com.nabil.flowmessenger.ui.theme.AppTextPrimaryColor
import java.util.UUID


@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    userListViewModel: UserListViewModel,
    onSelectUser : (UserData) -> Unit
) {
    val userList = userListViewModel.userList.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackgroundColor)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = AppTextPrimaryColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        HorizontalDivider(
            color = AppDarkSurfaceColor,
            thickness = 1.dp
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(userList.value, key = { it.userId ?: UUID.randomUUID() }) { user ->

                UserListItem(user = user){
                    onSelectUser(it)
                }

                HorizontalDivider(
                    modifier = Modifier.padding(start = 80.dp),
                    color = AppDarkSurfaceColor,
                    thickness = 1.dp
                )

            }

        }
    }
}


@Composable
fun UserListItem(user: UserData, onSelectUser: (UserData) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = { onSelectUser(user) })
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AppSurfaceColor)
            ) {
                AsyncImage(
                    model = user.userProfilePicUri,
                    contentDescription = user.userName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }


        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = user.userName.toString(),
                        color = AppTextPrimaryColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = user.userEmail.toString(),
                        color = AppTextPrimaryColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }


            }

        }
    }
}