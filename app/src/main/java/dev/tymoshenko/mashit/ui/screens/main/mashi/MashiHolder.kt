package dev.tymoshenko.mashit.ui.screens.main.mashi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth

@Composable
fun MashiHolder(
    modifier: Modifier = Modifier,
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight
) {
    AsyncImage(
        modifier = modifier
            .width(width)
            .height(height)
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