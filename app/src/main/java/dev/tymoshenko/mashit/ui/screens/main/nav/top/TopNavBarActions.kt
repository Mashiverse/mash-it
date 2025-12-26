package dev.tymoshenko.mashit.ui.screens.main.nav.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.ui.theme.AccountInfoShape
import dev.tymoshenko.mashit.ui.theme.ContainerColor
import dev.tymoshenko.mashit.ui.theme.ContentTextSize
import dev.tymoshenko.mashit.ui.theme.DrawerPaddingSize
import dev.tymoshenko.mashit.ui.theme.SearchHeight
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize

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
        }
    )

    Row(
        modifier = Modifier
            .padding(horizontal = SmallPaddingSize),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1F))

        AnimatedVisibility(
            visible = !isSearch,
            enter = expandHorizontally() + fadeIn(tween(delayMillis = 300, durationMillis = 150)),
            exit = shrinkOut() + fadeOut(tween(durationMillis = 0)) //slideOutHorizontally(targetOffsetX = { it -> -it* 2 }),
        ) {
            Row {
                OutlinedButton(
                    modifier = Modifier
                        .height(SearchHeight),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = Color.Red,
                    ),
                    border = BorderStroke(width = (1.3).dp, color = ContainerColor),
                    shape = AccountInfoShape,
                    contentPadding = PaddingValues(horizontal = SmallPaddingSize),
                    onClick = onDisconnect
                ) {
                    Text(
                        text = "${wallet.take(6)}...${wallet.substring(wallet.length - 4)}",
                        fontSize = ContentTextSize
                    )
                }

                Spacer(Modifier.width(DrawerPaddingSize))
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