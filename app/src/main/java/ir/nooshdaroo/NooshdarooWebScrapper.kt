package ir.nooshdaroo

import java.net.URL

@JvmInline
value class UrlPath(val value: String)

data class Headline(
    val subhead: String?,
    val title: String
)

data class Article(
    val category: Pair<String, URL>?,
    val imageUrl: URL,
    val articleUrl: URL,
    val headline: Headline,
    val readingTime: String?
)

data class ShortVideo(
    val duration: String?,
    val posterUrl: URL,
    val videoUrl: URL
)

interface NooshdarooWebScrapper {
    suspend fun extractContentCategoriesWithPath(): List<Pair<String, UrlPath>>
    suspend fun extractFeaturedContents(): List<Article>
    suspend fun extractShortVideos(): List<ShortVideo>
    suspend fun extractLatestContentInDigitalLiteracy(): List<Article>
}
