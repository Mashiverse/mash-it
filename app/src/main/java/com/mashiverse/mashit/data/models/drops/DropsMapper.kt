package com.mashiverse.mashit.data.models.drops

import com.mashiverse.mashit.data.remote.dtos.drops.DropsDto

fun DropsDto.toDrops() = this.drops.map { drop ->
    DropDetails(
        slug = drop.slug,
        imageUrl = drop.thumbnailUrl
    )
}