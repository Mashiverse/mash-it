package com.mashiverse.mashit.utils.helpers.nft

import com.mashiverse.mashit.data.models.nft.NftDetails

fun parseName(name: String): NftDetails {
    val (fullName: String, authorName: String) = if (" by " in name) {
        val parts = name.split(" by ", limit = 2)
        parts[0] to parts[1]
    } else {
        "" to ""
    }

    val (name: String, mint: Int) = if (" #" in fullName) {
        val parts = fullName.split(" #", limit = 2)
        parts[0] to parts[1].toInt()
    } else {
        "" to -1
    }

    return NftDetails(
        authorName = authorName,
        name = name,
        mint = mint
    )
}