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

val ervindasExample = MashiDetails(
    name = "Divine Flame",
    author = "Ervindas",
    description = "In time, the old ones perished. A new age began. From the ashes, they rose—not as Eternals, but as Divines. More brutal. More relentless. Utterly unbound.",
    perWallet = 12,
    soldQuantity = 12,
    quantity = 12,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmQJpSXW3wAK6KB9tSia6iGyyLezdEARsDPrVyUf2r9Rwx",
    traits = ervindasTraitsExample,
    price = 50
)

val napsExample = MashiDetails(
    name = "Plague “Dream”",
    author = "naps",
    description = "Every cycle begins the same way: Not with destruction. Not with war. But with doubt. Plague awakens when perfection lasts too long. When systems believe they are stable. When creators grow confident. That is when Plague dreams. And when Plague dreams, reality listens.",
    perWallet = 2,
    soldQuantity = 12,
    quantity = 12,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmRwncrk6mnHZUY2LqB7sm7eAfHEKdBmB9U1mXHRZxXCp3",
    traits = napsTraitsExample,
    price = 50
)

val panExample = MashiDetails(
    name = "Divine Flame",
    author = "Ervindas",
    description = "Every time she draws the sword, it steals a memory — and she keeps fighting so no one else has to forget.",
    perWallet = 3,
    soldQuantity = 4,
    quantity = 20,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmTkZeHLzw8zFwAaU8z6jCF46jDqae8gGiqnZkmuYd77uU",
    traits = panTraitsExample,
    price = 10
)

val hofExample = MashiDetails(
    name = "Nozomi",
    author = "Hofmaurerad",
    description = "May your days shine bright like festival lights...and may hope find you in every moment.",
    perWallet = 3,
    soldQuantity = 16,
    quantity =33,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmP1abhYkeghmyaPfZhzBEEMfgPPgeMEkZgQNTqnWsJKpP",
    traits = hofTraitsExample,
    price = 10
)

val rinniExample = MashiDetails(
    name = "Morika",
    author = "Rinni",
    description = "",
    perWallet = 5,
    soldQuantity = 16,
    quantity =49,
    compositeUrl = "https://ipfs.filebase.io/ipfs/QmP1abhYkeghmyaPfZhzBEEMfgPPgeMEkZgQNTqnWsJKpP",
    traits = rinniTraitsExample,
    price = 5
)

val multipleExample = listOf(
    ervindasExample,
    napsExample,
    panExample,
    hofExample,
    rinniExample
)