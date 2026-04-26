package ir.nooshdaroo.ui.model

import ir.nooshdaroo.Url

data class FeaturedTopContent(
    val categoryTitle: String,
    val imageUrl: Url,
    val subhead: String,
    val title: String,
    val articleUrl: Url
)
