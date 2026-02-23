package com.mashiverse.mashit.ui.screens.components.nav.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mashiverse.mashit.nav.routes.MainRoutes

import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.ContentTextSize
import com.mashiverse.mashit.ui.theme.PaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
private fun OtherSectionButton(
    onClick: () -> Unit,
    text: String,
    contentColor: Color = ContentColor,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(ContentContainerHeight),
        onClick = onClick,
        shape = ContentContainerShape,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent,
            contentColor = contentColor
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .offset(x = (-6).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontSize = ContentTextSize, fontWeight = FontWeight.Normal)
        }
    }
}

@Composable
fun OtherSection(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingSize)
    ) {
        OtherSectionButton(
            onClick = {
                navController.navigate(route = MainRoutes.Settings) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
                scope.launch {
                    drawerState.close()
                }
            },
            text = "Settings"
        )

//        OtherSectionButton(
//            onClick = {},
//            text = "Mash-It Rules"
//        )
//
//        OtherSectionButton(
//            onClick = {},
//            text = "Help Center",
//            contentColor = Color.Red
//        )
//
//        OtherSectionButton(
//            onClick = {},
//            text = "Sign Out",
//        )
    }
}