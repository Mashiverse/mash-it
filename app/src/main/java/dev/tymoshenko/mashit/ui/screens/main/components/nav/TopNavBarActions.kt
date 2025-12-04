package dev.tymoshenko.mashit.ui.screens.main.components.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopNavBarActions(
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    wallet: String,
    onDisconnect: () -> Unit
) {
    val endPadding = animateFloatAsState(
        targetValue = if (!isSearch) {
            0F
        } else {
            1F
        },
        tween(delayMillis = 300)
    )

    Row {
        Spacer(Modifier.weight(1F))

        AnimatedVisibility(
            visible = !isSearch,
            enter = expandHorizontally() + fadeIn(tween(delayMillis = 600)),
            exit = fadeOut() //slideOutHorizontally(targetOffsetX = { it -> -it* 2 }),
        ) {
            Row {
                OutlinedButton(
                    modifier = Modifier
                        .height(32.dp),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = Color.Red
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(8.dp),
                    onClick = onDisconnect
                ) {
                    Text( "${wallet.take(6)}...${wallet.substring(wallet.length - 4)}")
                }

                Spacer(Modifier.width(16.dp))
            }
        }

        SearchBar(
            isSearch = isSearch,
            onIsSearchChange = onIsSearchChange,
            searchQuery = searchQuery,
            onSearchQueryChange
        )

        if (endPadding.value != 0F) {
            Spacer(Modifier.weight(endPadding.value))
        }
    }
}