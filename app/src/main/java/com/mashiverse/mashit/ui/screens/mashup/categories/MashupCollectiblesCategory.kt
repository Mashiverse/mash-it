package com.mashiverse.mashit.ui.screens.mashup.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent

@Composable
fun MashupCollectiblesCategory(
    nfts: List<Nft>,
    state: LazyListState,
    mashupDetails: MashupDetails,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(nfts.size) { i ->
            val nft = nfts[i]

            MashupCollectiblesCategoryItem(
                nft = nft,
                mashupDetails = mashupDetails,
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent
            )
        }
    }
}