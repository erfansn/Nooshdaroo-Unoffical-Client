package ir.nooshdaroo.model

import ir.nooshdaroo.Url

data class Article(
    val imageUrl: Url,
    val description: Description,
    val readingTime: String? = null,
    val articleUrl: Url
)