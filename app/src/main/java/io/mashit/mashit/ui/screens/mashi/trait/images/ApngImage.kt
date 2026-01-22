package io.mashit.mashit.ui.screens.mashi.trait.images

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.penfeizhou.animation.apng.APNGDrawable
import com.github.penfeizhou.animation.loader.FileLoader
import java.io.File

@Composable
fun ApngImage(
    url: String,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    var apngDrawable by remember { mutableStateOf<APNGDrawable?>(null) }

    LaunchedEffect(url) {
        Glide.with(ctx)
            .asFile()
            .load(url)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    val loader = FileLoader(resource.absolutePath)
                    val drawable = APNGDrawable(loader)
                    drawable.start()
                    apngDrawable = drawable
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    apngDrawable?.stop()
                    apngDrawable = null
                }
            })
    }

    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        },
        update = { imageView ->
            if (apngDrawable != null && imageView.drawable !== apngDrawable) {
                imageView.setImageDrawable(apngDrawable)
            }
        },
        modifier = modifier
    )

    DisposableEffect(url) {
        onDispose {
            apngDrawable?.stop()
        }
    }
}