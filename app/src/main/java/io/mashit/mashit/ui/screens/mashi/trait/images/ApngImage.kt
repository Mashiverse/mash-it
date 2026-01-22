package io.mashit.mashit.ui.screens.mashi.trait.images

import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.penfeizhou.animation.apng.APNGDrawable
import com.github.penfeizhou.animation.loader.StreamLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL

class RemoteByteStreamLoader(private val data: ByteArray) : StreamLoader() {
    override fun getInputStream(): InputStream = ByteArrayInputStream(data)
}

@Composable
fun ApngImage(
    url: String,
    modifier: Modifier = Modifier
) {
    var drawable by remember { mutableStateOf<APNGDrawable?>(null) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            try {
                val data = URL(url).readBytes()
                val loader = RemoteByteStreamLoader(data)
                val apngDrawable = APNGDrawable(loader)
                apngDrawable.start() // start animation
                drawable = apngDrawable
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        },
        update = { imageView ->
            drawable?.let { imageView.setImageDrawable(it) }
        },
        modifier = modifier
    )
}
