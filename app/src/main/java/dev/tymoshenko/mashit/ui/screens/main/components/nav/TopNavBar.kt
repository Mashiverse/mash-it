package dev.tymoshenko.mashit.ui.screens.main.components.nav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.ui.theme.MashItTheme

@Composable
fun TopNavBar() {
    var isSearch by remember {
        mutableStateOf(false)
    }
    val onIsSearchChange = remember {
        { isSearch = !isSearch }
    }

    var searchQuery by remember { mutableStateOf("") }
    val onSearchQueryChange = remember {
        { input: String ->
            searchQuery = input
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TopNavBarActions(
                isSearch = isSearch,
                onIsSearchChange = onIsSearchChange,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange
            )

            Image(
                painter = painterResource(R.drawable.logo),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterStart),
                contentDescription = "logo"
            )
        }

        Spacer(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
    }
}

@Preview
@Composable
private fun TopNavBarPreview() {
    MashItTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .background(Color(16, 16, 16))
        ) {
            TopNavBar()
        }
    }
}