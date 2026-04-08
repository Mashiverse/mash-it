package com.mashiverse.mashit.ui.screens.artists.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.mashiverse.mashit.nav.routes.ArtistsRoutes
import com.mashiverse.mashit.ui.screens.shop.items.SectionLoading
import com.mashiverse.mashit.ui.screens.shop.items.SectionRefresh
import com.mashiverse.mashit.ui.theme.Padding

@Composable
fun ArtistsPreview(
    navController: NavHostController,
) {
    val viewModel = hiltViewModel<ArtistsPreviewViewModel>()
    val artists =
        viewModel.artistsPagingData.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val appendState = artists.loadState.append

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(Padding)
            ) {
                items(
                    count = artists.itemCount,
                    key = artists.itemKey { it.alias }
                ) { index ->
                    val artist = artists[index]

                    artist?.let {
                        val onClick = {
                            navController.navigate(route = ArtistsRoutes.Page(artist.alias))
                        }

                        ArtistsPreviewItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onClick,
                            artistInfo = artist,
                            processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                        )
                    }
                }

                if (appendState is LoadState.Loading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        SectionLoading()
                    }
                }

                if (appendState is LoadState.Error) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        SectionRefresh(
                            onRetry = { artists.retry() }
                        )
                    }
                }
            }
        }
    }
}