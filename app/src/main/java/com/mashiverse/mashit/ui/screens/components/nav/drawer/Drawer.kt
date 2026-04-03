package com.mashiverse.mashit.ui.screens.components.nav.drawer

import android.R
import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.common.primitives.Doubles.min
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.ui.theme.DrawerPaddingSize
import com.mashiverse.mashit.ui.theme.Padding
import kotlinx.coroutines.CoroutineScope

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun NavDrawer(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    wallet: String?,
    onConnect: () -> Unit,
) {
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    DismissibleDrawerSheet(
        drawerContainerColor = Color(32, 32, 32),
        drawerShape = RectangleShape,
        modifier = modifier
            .width(min((config.screenWidthDp * 0.80), 328.0).dp)
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
    ) {
        Spacer(modifier = Modifier.height(Padding))

        Column(
            verticalArrangement = Arrangement.spacedBy(Padding)
        ) {
            Row (
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(48.dp),
                    painter = painterResource(com.mashiverse.mashit.R.drawable.logo),
                    contentDescription = ""
                )


                wallet?.let {
                    Text(
                        text = wallet.lowercase(),
                        fontSize = 20.sp,
                        color = Color.Red,
                        maxLines = 1,
                        overflow = TextOverflow.MiddleEllipsis
                    )
                }
            }

//            Spacer(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(1.dp)
//                    .background(Color.DarkGray)
//            )


//
//
//            CreatePostButton(modifier = Modifier.padding(horizontal = DrawerPaddingSize))
//
//            HorizontalDivider()

            DrawerNav(
                modifier = Modifier.padding(horizontal = Padding),
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Padding)
                    .height(1.dp)
                    .background(Color.DarkGray)
            )

            BaseButton(
                onConnect = onConnect,
                wallet = wallet
            )


//            HorizontalDivider()
//
//            PinnedArtists(modifier = Modifier.padding(horizontal = DrawerPaddingSize))
//
//            HorizontalDivider()
//
//            OtherSection(
//                modifier = Modifier.padding(horizontal = DrawerPaddingSize),
//                navController = navController,
//                drawerState = drawerState,
//                scope = scope
//            )
        }
    }
}