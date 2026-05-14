package ir.nooshdaroo

import ir.nooshdaroo.data.model.Article
import ir.nooshdaroo.data.model.Category
import ir.nooshdaroo.data.model.Content
import ir.nooshdaroo.data.model.ShortVideo
import ir.nooshdaroo.data.model.Video

sealed interface MainUiState {
    data object Loading : MainUiState
    data object NetworkError : MainUiState
    data class Loaded(
        val mainContent: MainContent,
        val isRefreshingContent: Boolean,
        val isOffline: Boolean,
    ) : MainUiState
}

data class MainContent(
    val categories: List<Category> = emptyList(),
    val featuredContents: List<Content> = emptyList(),
    val latestContents: List<Content> = emptyList(),
    val latestShortVideos: List<ShortVideo> = emptyList(),
    val latestVideos: List<Video> = emptyList(),
    val latestDigitalLiteracyArticles: List<Article> = emptyList(),
    val dailyNote: String = "",
    val latestParentalControl: List<Article> = emptyList(),
    val emergencyArticles: List<Article> = emptyList(),
    val popularContents: List<Content> = emptyList()
)
