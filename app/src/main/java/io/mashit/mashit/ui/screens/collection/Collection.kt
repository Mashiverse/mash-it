package io.mashit.mashit.ui.screens.collection

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.mashit.mashit.data.models.image.ImageType
import io.mashit.mashit.data.models.mashi.MashiDetails
import io.mashit.mashit.data.models.mashi.mappers.fromEntities
import io.mashit.mashit.data.models.wallet.WalletPreferences
import io.mashit.mashit.data.remote.dtos.AlchemyDto
import io.mashit.mashit.ui.screens.header.CategoryHeader
import io.mashit.mashit.ui.screens.mashi.MashiBottomSheet
import io.mashit.mashit.ui.screens.mashi.trait.Trait
import io.mashit.mashit.ui.screens.placeholder.NotConnected
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.MashiHolderShape
import io.mashit.mashit.ui.theme.PaddingSize
import io.mashit.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.flow.map


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Collection() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<CollectionViewModel>()
    val mashies by viewModel.collectionFlow
        .map { it.fromEntities() }
        .collectAsState(emptyList())

    val walletPreferences = viewModel.walletPreferences.collectAsState(WalletPreferences(null))

    val selectedMashi by remember {
        derivedStateOf {
            viewModel.selectedMashi.value
        }
    }
    val selectMashi = { mashi: MashiDetails ->
        viewModel.selectMashi(mashi)
        isBottomSheet = true
    }

    val closeBottomShit = {
        isBottomSheet = false
    }

    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    CategoryHeader("Collection")

    Spacer(modifier = Modifier.height(PaddingSize))

    if (walletPreferences.value.wallet != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = PaddingSize),
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                columns = GridCells.Fixed(3)
            ) {
                items(mashies.size) { i ->
                    Trait(
                        modifier = Modifier
                            .height(mashiHolderHeight)
                            .width(mashiHolderWidth)
                            .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
                        onClick = { selectMashi.invoke(mashies[i]) },
                        data = mashies[i].compositeUrl,
                        getImageType = { url: String ->
                            var imageType: ImageType? = null
                            viewModel.getTraitTypeEntity(url) { type: ImageType? ->
                                imageType = type
                            }
                            imageType
                        },
                        setImageType = { imageType: ImageType, data: String ->
                            viewModel.insertTraitType(
                                url = data,
                                imageType = imageType
                            )
                        }
                    )
                }
            }
        }

        if (isBottomSheet) {
            selectedMashi?.let {
                MashiBottomSheet(
                    selectedMashi = selectedMashi!!,
                    sheetState = sheetState,
                    closeBottomShit = closeBottomShit,
                    scope = scope,
                    getImageType = { url: String ->
                        var imageType: ImageType? = null
                        viewModel.getTraitTypeEntity(url) { type: ImageType? ->
                            imageType = type
                        }
                        imageType
                    },
                    setImageType = { imageType: ImageType, data: String ->
                        viewModel.insertTraitType(
                            url = data,
                            imageType = imageType
                        )
                    }
                )
            }
        }
    } else {
        NotConnected()
    }
}