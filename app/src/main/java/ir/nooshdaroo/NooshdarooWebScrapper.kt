package ir.nooshdaroo

import android.util.Patterns

@JvmInline
value class Url(val address: String) {

    init {
        require(address.contains("localhost") || Patterns.WEB_URL.matcher(address).matches()) { "Invalid url address $address" }
    }

    val path: String?
        get() {
            var count = 3
            var cIndex = 0
            for ((index, ch) in address.withIndex()) {
                if (ch == '/') {
                    cIndex = index
                    count--
                }
                if (count == 0) break
            }

            return if (count != 0 || cIndex == address.lastIndex) {
                null
            } else {
                address.substring(cIndex).substringBeforeLast("#").substringBeforeLast("?")
            }
        }
}

data class Category(
    val title: String,
    val url: Url? = null
)

data class Description(
    val title: String,
    val subhead: String? = null
)

data class Article(
    val imageUrl: Url,
    val description: Description,
    val readingTime: String? = null,
    val articleUrl: Url
)

data class Video(
    val description: Description,
    val videoUrl: Url,
    val posterUrl: Url,
    val duration: String?
)

data class Content(
    val category: Category,
    val article: Article
)

data class ShortVideo(
    val duration: String? = null,
    val posterUrl: Url,
    val videoUrl: Url
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
    suspend fun extractPopularContents(): List<Content>
    suspend fun extractShouldKnowInEmergencyStateArticles(): List<Article>
}
