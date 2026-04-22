package ir.nooshdaroo

import ir.nooshdaroo.model.Article
import ir.nooshdaroo.model.Category
import ir.nooshdaroo.model.Content
import ir.nooshdaroo.model.ShortVideo
import ir.nooshdaroo.model.Video

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
