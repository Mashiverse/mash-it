package com.mashiverse.mashit.ui.screens.components.nav.top

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.ui.screens.components.nav.search.SearchBar

import com.mashiverse.mashit.ui.theme.Padding

@Composable
fun TopNavBarActions(
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = Padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1F))

        SearchBar(
            isSearch = isSearch,
            onIsSearchChange = onIsSearchChange,
            searchQuery = searchQuery,
            onSearchQueryChange
        )
    }
}

//AnimatedVisibility(
//visible = !isSearch,
//enter = expandHorizontally() + fadeIn(tween(delayMillis = 300, durationMillis = 150)),
//exit = shrinkOut() + fadeOut(tween(durationMillis = 0))
//) {
//    Row {
//        OutlinedButton(
//            modifier = Modifier
//                .height(SearchHeight),
//            colors = ButtonDefaults.outlinedButtonColors().copy(
//                containerColor = Color.Transparent,
//                contentColor = Color.Red,
//            ),
//            border = BorderStroke(width = 2.dp, color = Surface),
//            shape = AccountInfoShape,
//            contentPadding = PaddingValues(horizontal = Padding),
//            onClick = onConnect
//        ) {
//            Text(
//                text = if (wallet != null) {
//                    "${wallet.take(6).lowercase()}...${wallet.substring(wallet.length - 4).lowercase()}"
//                } else {
//                    "Connect Base"
//                },
//                fontSize = 20.sp
//            )
//        }
//
//        Spacer(Modifier.width(DrawerPaddingSize))
//    }
//}