package dev.tymoshenko.mashit.utils.color.helpers

fun replaceColors(
    svgSrc: String,
    bodyColor: String,
    eyesColor: String,
    hairColor: String
): String {
    var svgStr = Regex(
        "#00ff00|#0f0\\b|\\blime\\b|rgb\\s*\\(\\s*0\\s*,\\s*255\\s*,\\s*0\\s*\\)",
        RegexOption.IGNORE_CASE
    ).replace(svgSrc, bodyColor)

    svgStr = Regex(
        "#ffff00|#ff0\\b|\\byellow\\b|rgb\\s*\\(\\s*255\\s*,\\s*255\\s*,\\s*0\\s*\\)",
        RegexOption.IGNORE_CASE
    ).replace(svgStr, eyesColor)

    svgStr = Regex(
        "#0000ff|#00f\\b|\\bblue\\b|rgb\\s*\\(\\s*0\\s*,\\s*0\\s*,\\s*255\\s*\\)",
        RegexOption.IGNORE_CASE
    ).replace(svgStr, hairColor)

    return svgStr
}

fun makeStylingUnique(svg: String, suffix: String): String {
    var result = svg

    // 1. Rename all IDs
    result = result.replace(Regex("id=\"([^\"]+)\"")) { matchResult ->
        val originalId = matchResult.groupValues[1]
        """id="${originalId}_$suffix""""
    }

    // 2. Update all url(#id) references
    result = result.replace(Regex("url\\(#([^)]+)\\)")) { matchResult ->
        val originalId = matchResult.groupValues[1]
        "url(#${originalId}_$suffix)"
    }

    // 3. Update href references
    result = result.replace(Regex("href=\"#([^\"]+)\"")) { matchResult ->
        val originalId = matchResult.groupValues[1]
        "href=\"#${originalId}_$suffix\""
    }

    // 4. Rename classes inside <style> blocks
    result = result.replace(Regex("<style>(.*?)</style>", RegexOption.DOT_MATCHES_ALL)) { matchResult ->
        val styleContent = matchResult.groupValues[1]
        val updatedStyle = styleContent.replace(Regex("\\.([a-zA-Z0-9_-]+)")) { clsMatch ->
            ".${clsMatch.groupValues[1]}_$suffix"
        }
        "<style>$updatedStyle</style>"
    }

    // 5. Update class attributes in SVG elements
    result = result.replace(Regex("class=\"([^\"]+)\"")) { matchResult ->
        val originalClass = matchResult.groupValues[1]
        "class=\"${originalClass}_$suffix\""
    }

    return result
}
