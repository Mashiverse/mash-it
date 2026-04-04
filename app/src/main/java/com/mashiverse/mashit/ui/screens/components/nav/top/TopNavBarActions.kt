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