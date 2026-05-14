package com.mashiverse.mashit.ui.screens.artists.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.artists.ArtistInfo
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.screens.artists.ProfilePicture
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallPadding


@Composable
fun ArtistsPreviewItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    artistInfo: ArtistInfo,
    processImageIntent: (ImageIntent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Padding))
            .clickable {
                onClick.invoke()
            }
            .background(Secondary.copy(alpha = 0.33f)),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .clip(RoundedCornerShape(MediumPadding))
                .background(Secondary)
                .clickable {
                    onClick.invoke()
                }
                .padding(SmallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfilePicture(
                onClick = onClick,
                artistMashup = artistInfo.mashup,
                processImageIntent = processImageIntent
            )

            Spacer(modifier = Modifier.width(Padding))

            Text(
                text = artistInfo.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}