package ir.nooshdaroo.model

import ir.nooshdaroo.Url

data class ShortVideo(
    val duration: String? = null,
    val posterUrl: Url,
    val videoUrl: Url
)