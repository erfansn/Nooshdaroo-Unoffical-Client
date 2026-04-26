package ir.nooshdaroo.data

import ir.nooshdaroo.data.model.Article
import ir.nooshdaroo.data.model.Category
import ir.nooshdaroo.data.model.Content
import ir.nooshdaroo.data.model.ShortVideo
import ir.nooshdaroo.data.model.Video

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
