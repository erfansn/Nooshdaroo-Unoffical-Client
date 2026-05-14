package ir.nooshdaroo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ir.nooshdaroo.data.NooshdarooWebFetcherImpl
import ir.nooshdaroo.data.NooshdarooWebScrapperImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val nooshdarooWebFetcher = NooshdarooWebFetcherImpl(application)
    private val nooshdarooWebScrapper = NooshdarooWebScrapperImpl(nooshdarooWebFetcher)

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)

    val uiState = _uiState.onStart {
        initialLoad()
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )

    fun refreshContent() {
        viewModelScope.launch {
            nooshdarooWebFetcher.invalidCache()

            _uiState.updateAsLoaded {
                it.copy(isRefreshingContent = true)
            }
            val updatedContent = MainContent(
                categories = nooshdarooWebScrapper.extractContentCategoriesWithPath(),
                featuredContents = nooshdarooWebScrapper.extractFeaturedContents(),
                latestContents = nooshdarooWebScrapper.extractLatestContent(),
                latestShortVideos = nooshdarooWebScrapper.extractShortVideos(),
                latestVideos = nooshdarooWebScrapper.extractLatestVideos(),
                latestDigitalLiteracyArticles = nooshdarooWebScrapper.extractLatestContentInDigitalLiteracy(),
                dailyNote = nooshdarooWebScrapper.extractDailyNote(),
                latestParentalControl = nooshdarooWebScrapper.extractLatestInParentalControl(),
                emergencyArticles = nooshdarooWebScrapper.extractShouldKnowInEmergencyStateArticles(),
                popularContents = nooshdarooWebScrapper.extractPopularContents(),
            )
            _uiState.updateAsLoaded {
                it.copy(
                    mainContent = updatedContent,
                    isRefreshingContent = false
                )
            }
        }
    }

    private fun MutableStateFlow<MainUiState>.updateAsLoaded(update: (MainUiState.Loaded) -> MainUiState.Loaded) = this.update {
        require(it is MainUiState.Loaded)
        update(it)
    }

    fun initialLoad() {
        viewModelScope.launch {
            _uiState.update {
                val mainContent = runCatching {
                    MainContent(
                        categories = nooshdarooWebScrapper.extractContentCategoriesWithPath(),
                        featuredContents = nooshdarooWebScrapper.extractFeaturedContents(),
                        latestContents = nooshdarooWebScrapper.extractLatestContent(),
                        latestShortVideos = nooshdarooWebScrapper.extractShortVideos(),
                        latestVideos = nooshdarooWebScrapper.extractLatestVideos(),
                        latestDigitalLiteracyArticles = nooshdarooWebScrapper.extractLatestContentInDigitalLiteracy(),
                        dailyNote = nooshdarooWebScrapper.extractDailyNote(),
                        latestParentalControl = nooshdarooWebScrapper.extractLatestInParentalControl(),
                        emergencyArticles = nooshdarooWebScrapper.extractShouldKnowInEmergencyStateArticles(),
                        popularContents = nooshdarooWebScrapper.extractPopularContents(),
                    )
                }.getOrNull()

                if (mainContent != null) {
                    MainUiState.Loaded(mainContent = mainContent, isRefreshingContent = false)
                } else {
                    MainUiState.NetworkError
                }
            }
        }
    }
}