package com.mashiverse.mashit.ui.screens.mashup.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent
import com.mashiverse.mashit.ui.screens.components.nft.TraitHolder
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.TraitShape
import com.mashiverse.mashit.utils.helpers.detectScreenType
import com.mashiverse.mashit.utils.helpers.getItemWidthAndHeight

@Composable
fun MashupCollectiblesCategoryItem(
    nft: Nft,
    mashupDetails: MashupDetails,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()
    val (width, _) = config.getItemWidthAndHeight(screenType.collectionColumns, 12.dp)

    val isSelected = { trait: Trait ->
        val sameTypeTrait = mashupDetails.assets.find { it.type == trait.type }
        sameTypeTrait?.url == trait.url
    }


    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12))
                .clickable {
                    isExpanded = !isExpanded
                }
                .background(Secondary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .width((552 * 0.06).dp)
                    .height((736 * 0.06).dp)
                    .clip(TraitShape),
                model = nft.compositeUrl,
                contentDescription = null
            )

            Spacer(modifier = Modifier.weight(1F))

            Text(
                text = nft.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                onClick = {
                    isExpanded = !isExpanded
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    tint = ContentAccentColor,
                    imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                    contentDescription = null
                )
            }
        }

        AnimatedVisibility(isExpanded) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(11.5.dp),
                    maxItemsInEachRow = screenType.collectionColumns
                ) {
                    nft.traits?.forEach { trait ->
                        TraitHolder(
                            modifier = Modifier
                                .width(width),
                            isSelected = isSelected.invoke(trait),
                            trait = trait,
                            processImageIntent = processImageIntent,
                            onClick = {
                                processMashupIntent.invoke(
                                    MashupIntent.OnMashupUpdate(
                                        MashupTrait(trait = trait, avatarName = nft.name)
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}