package io.mashit.mashit.ui.screens.main.mashup.composite

import android.graphics.Color
import android.webkit.WebView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.data.models.mashi.TraitType
import io.mashit.mashit.utils.color.helpers.replaceColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import io.mashit.mashit.data.models.color.SelectedColors

suspend fun isSvgUrl(url: String): Boolean = withContext(Dispatchers.IO) {
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()
        val contentType = connection.contentType
        connection.disconnect()
        contentType != null && contentType.contains("image/svg+xml")
    } catch (e: Exception) {
        false
    }
}

suspend fun fetchSvgContent(url: String): String = withContext(Dispatchers.IO) {
    try {
        URL(url).readText()
    } catch (e: Exception) {
        ""
    }
}

@Composable
fun CompositeHolder(
    mashiTraits: List<MashiTrait?>,
    modifier: Modifier = Modifier,
    selectedColors: SelectedColors
) {
    var htmlContent by remember { mutableStateOf("") }

    LaunchedEffect(mashiTraits, selectedColors) {
        val htmlBuilder = StringBuilder()
        htmlBuilder.append(
            """
            <html>
              <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
                <style>
                  body { margin:0; padding:0; background:transparent; overflow:hidden; }
                  .container { position:relative; width:100%; aspect-ratio:3/4; }
                  .layer { position:absolute; top:0; left:0; width:100%; aspect-ratio:3/4; display:flex; justify-content:center; align-items:center; overflow:hidden; }
                  .trait-content { display:block; width:auto; height:81.5%; aspect-ratio:19/30; max-height:100%; }
                  .background-content { display:block; width:auto; height:100%; aspect-ratio:3/4; max-height:100%; }
                </style>
              </head>
              <body>
                <div class="container">
            """.trimIndent()
        )

        mashiTraits.filterNotNull().forEachIndexed { index, trait ->
            val traitClass = if (trait.traitType == TraitType.BACKGROUND) "background-content" else "trait-content"

            if (isSvgUrl(trait.url)) {
                val rawSvg = fetchSvgContent(trait.url)
                if (rawSvg.isNotEmpty()) {
                    // 1. Replace colors
                    val coloredSvg = replaceColors(
                        svgSrc = rawSvg,
                        bodyColor = selectedColors.body,
                        eyesColor = selectedColors.eyes,
                        hairColor = selectedColors.hair
                    )


                    // 3. Encode SVG in Base64 to avoid WebView parsing issues
                    val base64Svg = Base64.encodeToString(coloredSvg.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

                    htmlBuilder.append(
                        """
                        <div class="layer">
                            <img class="$traitClass" src="data:image/svg+xml;base64,$base64Svg" />
                        </div>
                        """.trimIndent()
                    )
                }
            } else {
                htmlBuilder.append(
                    """
                    <div class="layer">
                        <img class="$traitClass" src="${trait.url}" />
                    </div>
                    """.trimIndent()
                )
            }
        }

        htmlBuilder.append("</div></body></html>")
        htmlContent = htmlBuilder.toString()
    }

    if (htmlContent.isNotEmpty()) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = false
                    settings.loadsImagesAutomatically = true
                    setBackgroundColor(Color.TRANSPARENT)
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
            }
        )
    }
}
