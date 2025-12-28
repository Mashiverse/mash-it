package dev.tymoshenko.mashit.ui.screens.main.mashup.composite

import android.graphics.Color
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import dev.tymoshenko.mashit.data.models.mashi.Trait
import dev.tymoshenko.mashit.data.models.mashi.TraitType
import dev.tymoshenko.mashit.ui.theme.LargeTraitHolderWidth
import dev.tymoshenko.mashit.utils.color.helpers.makeStylingUnique
import dev.tymoshenko.mashit.utils.color.helpers.replaceColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

// Detect if a URL is an SVG by checking the content type
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

// Download the SVG content as a string
suspend fun fetchSvgContent(url: String): String = withContext(Dispatchers.IO) {
    try {
        URL(url).readText()
    } catch (e: Exception) {
        ""
    }
}


@Composable
fun CompositeHolder(
    traits: List<Trait?>,
    modifier: Modifier = Modifier,
    bodyColor: String = "#00FF00",
    eyesColor: String = "#FFFF00",
    hairColor: String = "#0000FF"
) {
    var htmlContent by remember { mutableStateOf("") }

    // Prepare HTML content asynchronously
    LaunchedEffect(traits, bodyColor, eyesColor, hairColor) {
        val htmlBuilder = StringBuilder()
        htmlBuilder.append(
            """
    <html>
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <style>
          body { margin: 0; padding: 0; background: transparent; overflow: hidden; }
          
          /* The container width is 100% of the WebView. 
             We use aspect-ratio 1/1 so the Background defines the height. */
          .container { 
            position: relative; 
            width: 100%; 
            aspect-ratio: 3 / 4; 
          }
          
          .layer {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            aspect-ratio: 3 / 4;
            overflow: hidden;
            display: flex;
            justify-content: center;
            align-items: center;
          }
          
          .trait-content {
            display: block;
            width: auto;      
            height: 81.5%; /* scaled relative to layer height */
            aspect-ratio: 19 / 30; /* content aspect ratio */
            max-height: 100%;
          }
          
          .background-content {
            display: block;
            width: auto;      
            height: 100%;
            aspect-ratio: 3 / 4;
            max-height: 100%;
          }          
          
        </style>
      </head>
      <body>
        <div class="container">
    """.trimIndent()
        )

        traits
            .filter{trait -> trait != null}
            .forEachIndexed { index, trait ->
            val traitClass = if (trait!!.traitType == TraitType.BACKGROUND) {
                "background-content"
            } else {
                "trait-content"
            }

            if (isSvgUrl(trait.url)) {
                val rawSvg = fetchSvgContent(trait.url)
                if (rawSvg.isNotEmpty()) {
                    // 1. Color Replacement
                    val coloredSvg = replaceColors(rawSvg, bodyColor, eyesColor, hairColor)

                    // 2. Fix ID Conflict

                    htmlBuilder.append("""
                        <div class="layer">
                            <div class="$traitClass">
                                ${makeStylingUnique(coloredSvg, index.toString())}
                            </div>
                        </div>""".trimIndent())
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

    // Render the HTML in a WebView
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
                webView.loadDataWithBaseURL(
                    null,
                    htmlContent,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        )
    }
}
