package com.mashiverse.mashit.ui.screens.shop

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.sys.data.ShopDataType
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.sys.DialogIntent
import com.mashiverse.mashit.nav.graphs.artistsGraph
import com.mashiverse.mashit.nav.graphs.shopGraph
import com.mashiverse.mashit.nav.routes.ArtistsRoutes
import com.mashiverse.mashit.nav.routes.ShopRoutes
import com.mashiverse.mashit.ui.default.dialogs.Dialog
import com.mashiverse.mashit.ui.default.modals.ItemPreviewModal
import com.mashiverse.mashit.ui.default.modals.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.shop.regular.RegularShopViewModel
import com.mashiverse.mashit.ui.screens.shop.sections.Category
import com.mashiverse.mashit.ui.screens.shop.sections.Drops
import com.mashiverse.mashit.ui.theme.Padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(
    searchQuery: State<String>,
    clearSearchQuery: () -> Unit,
    listingId: String?
) {
    val innerNavController = rememberNavController()

    val navigateToDrop = { slug: String ->
        innerNavController.navigate(route = ShopRoutes.SpecialShop(slug = slug))
    }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = innerNavController,
        startDestination = ShopRoutes.RegularShop(listingId = listingId)
    ) {
        shopGraph(
            clearSearchQuery = clearSearchQuery,
            searchQuery = searchQuery,
            navigateToDrop = navigateToDrop
        )
    }
}