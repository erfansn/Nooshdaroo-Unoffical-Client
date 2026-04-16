package ir.nooshdaroo

import java.net.URL

data class Category(val title: String, val url: URL)

data class Description(
    val title: String,
    val subhead: String? = null
)

data class Article(
    val imageUrl: URL,
    val description: Description,
    val readingTime: String? = null,
    val articleUrl: URL
)

data class Content(
    val category: Category,
    val article: Article
)

data class ShortVideo(
    val duration: String? = null,
    val posterUrl: URL,
    val videoUrl: URL
)

interface NooshdarooWebScrapper {
    suspend fun extractContentCategoriesWithPath(): List<Category>
    suspend fun extractFeaturedContents(): List<Content>
    suspend fun extractShortVideos(): List<ShortVideo>
    suspend fun extractLatestContentInDigitalLiteracy(): List<Article>
}
