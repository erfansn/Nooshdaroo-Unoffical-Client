package ir.nooshdaroo.model

import ir.nooshdaroo.Url

data class Category(
    val title: String,
    val url: Url? = null
)
