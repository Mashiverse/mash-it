package com.mashiverse.mashit.data.models.sys.screens

enum class ScreenInfo(
    val shopColumns: Int,
    val collectionColumns: Int
) {
    COMPACT(2, 3),
    MEDIUM(4, 6),
    EXPANDED(4, 6)
}