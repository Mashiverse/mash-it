package com.mashiverse.mashit.ui.default.grids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.traits.MintedTrait

@Composable
fun MintedTraitGrid(
    items: List<Nft>,
    state: LazyGridState,
    spacedByVert: Dp,
    spacedByHoriz: Dp,
    columns: Int,
    processImageIntent: (ImageIntent) -> Unit,
    onClick: (Nft) -> Unit
) {
    LazyVerticalGrid(
        state = state,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacedByVert),
        horizontalArrangement = Arrangement.spacedBy(spacedByHoriz),
        columns = GridCells.Fixed(columns)
    ) {
        items(items.size) { i ->
            MintedTrait(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClick.invoke(items[i]) },
                data = items[i].compositeUrl,
                processImageIntent = processImageIntent,
                mint = items[i].owned!![0].mint
            )
        }
    }
}