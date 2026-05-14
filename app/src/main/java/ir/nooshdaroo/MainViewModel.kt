package ir.nooshdaroo

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.nooshdaroo.data.NooshdarooWebScrapperImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import java.net.URL

class MainViewModel : ViewModel() {

    private val nooshdarooWebScrapper = NooshdarooWebScrapperImpl(URL(BASE_URL))

    val uiState = flow {
        val uiState = MainUiState(
            categories = nooshdarooWebScrapper.extractContentCategoriesWithPath(),
            featuredContents = nooshdarooWebScrapper.extractFeaturedContents(),
            latestContents = nooshdarooWebScrapper.extractLatestContent(),
            latestShortVideos = nooshdarooWebScrapper.extractShortVideos(),
            latestVideos = nooshdarooWebScrapper.extractLatestVideos(),
            latestDigitalLiteracyArticles = nooshdarooWebScrapper.extractLatestContentInDigitalLiteracy(),
            dailyNote = nooshdarooWebScrapper.extractDailyNote(),
            latestParentalControl = nooshdarooWebScrapper.extractLatestInParentalControl(),
            emergencyArticles = nooshdarooWebScrapper.extractShouldKnowInEmergencyStateArticles(),
            popularContents = nooshdarooWebScrapper.extractPopularContents()
        )
        emit(uiState)
    }.flowOn(Dispatchers.IO).stateIn(
        scope = viewModelScope,
        initialValue = MainUiState(
            categories = emptyList(),
            featuredContents = emptyList(),
            latestContents = emptyList(),
            latestShortVideos = emptyList(),
            latestVideos = emptyList(),
            latestDigitalLiteracyArticles = emptyList(),
            dailyNote = "",
            latestParentalControl = emptyList(),
            emergencyArticles = emptyList(),
            popularContents = emptyList()
        ),
        started = SharingStarted.WhileSubscribed(5_000)
    )
}