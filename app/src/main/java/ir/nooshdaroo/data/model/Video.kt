package ir.nooshdaroo.data.model

import ir.nooshdaroo.Url

data class Video(
    val description: Description,
    val videoUrl: Url,
    val posterUrl: Url,
    val duration: String?
)