package com.mashiverse.mashit.ui.default.modals

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Surface
import com.reown.appkit.ui.AppKitTheme
import com.reown.appkit.ui.components.internal.AppKitComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInModal(
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        shape = BottomSheetShape,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Surface,
        contentColor = ContentColor,
        dragHandle = null,
        sheetGesturesEnabled = false
    ) {
        AppKitTheme(
            mode = AppKitTheme.Mode.DARK
        ) {
            AppKitComponent(
                shouldOpenChooseNetwork = false,
                closeModal = onDismissRequest
            )
        }
    }
}