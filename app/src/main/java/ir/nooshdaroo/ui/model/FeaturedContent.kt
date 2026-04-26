package ir.nooshdaroo.ui.model

import ir.nooshdaroo.Url

data class FeaturedContent(
    val categoryTitle: String,
    val imageUrl: Url,
    val title: String,
    val readingTime: String,
    val articleUrl: Url
)
