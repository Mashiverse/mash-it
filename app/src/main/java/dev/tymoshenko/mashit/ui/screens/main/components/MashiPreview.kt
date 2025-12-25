package dev.tymoshenko.mashit.ui.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape

@Composable
fun MashiHolder(
    modifier: Modifier = Modifier
        .width((552 * 0.24).dp)
        .height((736 * 0.24).dp)
) {
    AsyncImage(
        modifier = modifier
            .clip(MashiHolderShape)
            .background(MashiBackground),
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://example.com/image.jpg")
            .crossfade(true)
            .build(),
        contentDescription = "Mashi",
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
private fun MashiHolderPreview() {
    MashiHolder()
}