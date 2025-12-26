package dev.tymoshenko.mashit.data.models.mashi

data class MashiDetails(
    val name: String,
    val author: String,
    val description: String,
    val perWallet: Int,
    val soldQuantity: Int,
    val quantity: Int,
    val compositeUrl: String,
    val traits: List<Trait>,
    val price: Int,
    val priceCurrency: PriceCurrency = PriceCurrency.USDC
)

val traitsExample = listOf(
    Trait(
        traitType = TraitType.BACKGROUND,
        url = "https://ipfs.filebase.io/ipfs/QmWCqFphy8GEPr5GowgmQZTucfmB7V7efQBDZQ99C8ESxY"
    ),
    Trait(
        traitType = TraitType.HAIR_BACK,
        url = "https://ipfs.filebase.io/ipfs/QmNo5pXHb3n1B2Cnp1acEpgmG17Kv9yXDmiBuXmdLmN1UU"
    ),
    Trait(
        traitType = TraitType.CAPE,
        url = "https://ipfs.filebase.io/ipfs/QmVDo7RbmGZy8wXBwKdtYy2cFUkGq1FXqD1SPdqF5bv4rt"
    ),
    Trait(
        traitType = TraitType.BOTTOM,
        url = "https://ipfs.filebase.io/ipfs/QmUgqLqiMU96kpVRXuAQRhp29eBi3X2k5V3J2QD1VVTDSv"
    ),
    Trait(
        traitType = TraitType.UPPER,
        url = "https://ipfs.filebase.io/ipfs/QmUh26EUKkyGGyx7Yf2ReTby8JWXPQyQTPbb6EoniptKNz"
    ),
    Trait(
        traitType = TraitType.HEAD,
        url = "https://ipfs.filebase.io/ipfs/QmP7gtqYd8yAMK2vCbJzHQ7KMxnURkhUgbT4oKXDx2nYuB"
    ),
    Trait(
        traitType = TraitType.EYES,
        url = "https://ipfs.filebase.io/ipfs/QmXbPEwiFGmy4VhbjjsSvbs2KqnQgkGBezwgPNQzFtudUK"
    ),
    Trait(
        traitType = TraitType.HAIR_FRONT,
        url = "https://ipfs.filebase.io/ipfs/Qmc9NrBWZR7J1xYeJQJ2NP1HAw4Uzwmwoh3dnowU8qi6V4"
    ),
    Trait(
        traitType = TraitType.HAT,
        url = "https://ipfs.filebase.io/ipfs/QmZTSZRrdszcoWyJxYPmzvFHADRvXaQTGVaSgpLa7RvES9"
    ),
    Trait(
        traitType = TraitType.LEFT_ACCESSORY,
        url = "https://ipfs.filebase.io/ipfs/QmUYr95SvARnDXrMgTF3dUo98Z1p91LEnqGMBNcfaXB4FU"
    ),
    Trait(
        traitType = TraitType.RIGHT_ACCESSORY,
        url = "https://ipfs.filebase.io/ipfs/QmTbBShnAf9kUpuVvs6sSbJ3ZGDRbxUcosUH7vwDzUsvJL"
    )
)

val ervindasExample = MashiDetails(
    name = "Divine Flame",
    author = "Ervindas",
    description = "In time, the old ones perished. A new age began. From the ashes, they rose—not as Eternals, but as Divines. More brutal. More relentless. Utterly unbound.",
    perWallet = 12,
    soldQuantity = 12,
    quantity = 12,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmQJpSXW3wAK6KB9tSia6iGyyLezdEARsDPrVyUf2r9Rwx",
    traits = traitsExample,
    price = 50
)

val ervindasExample2 = MashiDetails(
    name = "Divine Flame",
    author = "Ervindas",
    description = "In time, the old ones perished. A new age began. From the ashes, they rose—not as Eternals, but as Divines. More brutal. More relentless. Utterly unbound.",
    perWallet = 12220,
    soldQuantity = 11220,
    quantity = 12220,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmQJpSXW3wAK6KB9tSia6iGyyLezdEARsDPrVyUf2r9Rwx",
    traits = traitsExample,
    price = 50
)

val ervindasExample3 = MashiDetails(
    name = "Divine Flame",
    author = "Ervindas",
    description = "In time, the old ones perished. A new age began. From the ashes, they rose—not as Eternals, but as Divines. More brutal. More relentless. Utterly unbound.",
    perWallet = 12,
    soldQuantity = 12,
    quantity = 12,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmQJpSXW3wAK6KB9tSia6iGyyLezdEARsDPrVyUf2r9Rwx",
    traits = traitsExample,
    price = 50,
    priceCurrency = PriceCurrency.MATIC
)

val multipleExample = listOf(
    ervindasExample,
    ervindasExample2,
    ervindasExample3
)