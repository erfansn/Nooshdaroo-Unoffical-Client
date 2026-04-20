package ir.nooshdaroo

import java.net.URL

data class Category(
    val title: String,
    val url: URL? = null
)

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

data class Video(
    val description: Description,
    val videoUrl: URL,
    val posterUrl: URL,
    val duration: String?
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
    suspend fun extractLatestVideos(): List<Video>
    suspend fun extractLatestContent(): List<Content>
    suspend fun extractDailyNote(): String
    suspend fun extractLatestInParentalControl(): List<Article>
}
