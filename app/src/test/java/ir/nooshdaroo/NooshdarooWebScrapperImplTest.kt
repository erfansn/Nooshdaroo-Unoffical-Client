package ir.nooshdaroo

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NooshdarooWebScrapperImplTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private val mainPage = javaClass.getResourceAsStream("/index.html")!!.reader().use { it.readText() }

    private lateinit var nooshdarooWebScrapper: NooshdarooWebScrapper

    @Before
    fun setUp() {
        mockWebServer.enqueue(MockResponse().setBody(mainPage).setResponseCode(200))

        nooshdarooWebScrapper = NooshdarooWebScrapperImpl(mockWebServer.url("/").toUrl())
    }

    @Test
    fun mainPage_extractingCategories_returnsCorrectly() = runTest {
        val categories = nooshdarooWebScrapper.extractContentCategoriesWithPath()

        assertThat(categories).apply {
            hasSize(15)
        }
    }

    @Test
    fun mainPage_extractingFeaturedContents_returnsCorrectly() = runTest {
        val featuredContents = nooshdarooWebScrapper.extractFeaturedContents()

        assertThat(featuredContents).apply {
            hasSize(13)
        }
    }

    @Test
    fun mainPage_extractingShortVideos_returnsCorrectly() = runTest {
        val shortVideos = nooshdarooWebScrapper.extractShortVideos()

        assertThat(shortVideos).apply {
            hasSize(11)
        }
    }

    @Test
    fun mainPage_extractingLatestDigitalLiteracyContent_returnsCorrectly() = runTest {
        val latestDigitalLiteracyContent = nooshdarooWebScrapper.extractLatestContentInDigitalLiteracy()

        assertThat(latestDigitalLiteracyContent).apply {
            hasSize(9)
        }
    }

    @Test
    fun mainPage_extractingLatestVideos_returnsCorrectly() = runTest {
        val latestVideos = nooshdarooWebScrapper.extractLatestVideos()

        assertThat(latestVideos).apply {
            hasSize(5)
        }
    }

    @Test
    fun mainPage_extractingLatestContent_returnsCorrectly() = runTest {
        val latestContent = nooshdarooWebScrapper.extractLatestContent()

        assertThat(latestContent).apply {
            hasSize(3)
        }
    }

    @Test
    fun mainPage_extractingDailyNote_returnsCorrectly() = runTest {
        val todayNote = nooshdarooWebScrapper.extractDailyNote()

        assertThat(todayNote).isEqualTo("اگر در پیامکی خواندی که حساب بانکی\u200Cات مسدود شده، نخست ببین که اصلاً در آن بانک حساب داری یا نه!")
    }

    @Test
    fun mainPage_extractingLatestInParentalControl_returnsCorrectly() = runTest {
        val parentalControl = nooshdarooWebScrapper.extractLatestInParentalControl()

        assertThat(parentalControl).apply {
            hasSize(4)
        }
    }

    @Test
    fun mainPage_extractingPopularContents_returnsCorrectly() = runTest {
        val popularContents = nooshdarooWebScrapper.extractPopularContents()

        assertThat(popularContents).apply {
            hasSize(5)
        }
    }

    @Test
    fun mainPage_extractingShouldKnowInEmergencyStateArticles_returnsCorrectly() = runTest {
        val articles = nooshdarooWebScrapper.extractShouldKnowInEmergencyStateArticles()

        assertThat(articles).apply {
            hasSize(4)
        }
    }
}
