package ir.nooshdaroo

import ir.nooshdaroo.data.model.Article
import ir.nooshdaroo.data.model.Category
import ir.nooshdaroo.data.model.Content
import ir.nooshdaroo.data.model.ShortVideo
import ir.nooshdaroo.data.model.Video

data class MainUiState(
    val categories: List<Category>,
    val featuredContents: List<Content>,
    val latestContents: List<Content>,
    val latestShortVideos: List<ShortVideo>,
    val latestVideos: List<Video>,
    val latestDigitalLiteracyArticles: List<Article>,
    val dailyNote: String,
    val latestParentalControl: List<Article>,
    val emergencyArticles: List<Article>,
    val popularContents: List<Content>
)
