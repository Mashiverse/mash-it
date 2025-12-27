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