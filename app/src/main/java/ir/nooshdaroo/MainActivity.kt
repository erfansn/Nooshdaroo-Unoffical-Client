package ir.nooshdaroo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.nooshdaroo.data.model.Article
import ir.nooshdaroo.data.model.Category
import ir.nooshdaroo.data.model.Content
import ir.nooshdaroo.data.model.Description
import ir.nooshdaroo.data.model.ShortVideo
import ir.nooshdaroo.data.model.Video
import ir.nooshdaroo.ui.NooshdarooTheme
import ir.nooshdaroo.ui.PreviewNooshdarooTheme
import ir.nooshdaroo.ui.component.CategoryText
import ir.nooshdaroo.ui.component.ContentImage
import ir.nooshdaroo.ui.component.ContentItem
import ir.nooshdaroo.ui.component.ShortVideoItem
import ir.nooshdaroo.ui.component.VideoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sign
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        val desiredLocale = LocaleListCompat.create(Locale.forLanguageTag("fa-IR"))
        AppCompatDelegate.setApplicationLocales(desiredLocale)
        setContent {
            NooshdarooTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                MainScreen(uiState = uiState)
            }
        }
    }
}

const val BASE_URL = "https://nooshdaroo.ir/"

@Composable
private fun MainScreen(
    uiState: MainUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MainTopBar()
        },
        modifier = modifier,
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.safeDrawing.exclude(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
        ) {
            CategoriesRow(uiState.categories)
            val contentPadding = PaddingValues(horizontal = 16.dp)
            LazyColumn {
                item {
                    FeaturedContentsSection(
                        contentPadding = contentPadding,
                        contents = uiState.featuredContents
                    )
                }
                item {
                    LatestContentsSection(
                        contentPadding = contentPadding,
                        contents = uiState.latestContents,
                    )
                }
                item {
                    LatestShortVideosSection(
                        contentPadding = contentPadding,
                        shortVideos = uiState.latestShortVideos
                    )
                }
                item {
                    LatestVideosSection(
                        videos = uiState.latestVideos,
                        contentPadding = contentPadding
                    )
                }
                item {
                    LatestDigitalLiteracyContentSection(
                        contentPadding = contentPadding,
                        articles = uiState.latestDigitalLiteracyArticles
                    )
                }
                item {
                    DailyNoteSection(
                        note = uiState.dailyNote,
                        contentPadding = contentPadding
                    )
                }
                item {
                    LatestInParentalControlSection(
                        articles = uiState.latestParentalControl,
                        contentPadding = contentPadding
                    )
                }
                item {
                    PopularContentsSection(
                        contents = uiState.popularContents,
                        contentPadding = contentPadding
                    )
                }
                item {
                    EmergencyArticlesSection(
                        articles = uiState.emergencyArticles,
                        contentPadding = contentPadding
                    )
                }
                item {
                    AboutNooshdarooSection(
                        contentPadding = contentPadding
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 12.dp, start = 16.dp)
        )
        Text(stringResource(R.string.app_title), style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun CategoriesRow(
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        items(categories) {
            CategoryItem(
                title = it.title,
                onClick = {
                    uriHandler.openUri(it.url.address)
                }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(Color(0xFFF4F1EC))
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeaturedContentsSection(
    contents: List<Content>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF4F1EC))
            .padding(vertical = 24.dp)
    ) {
        val contentPaddingModifier = Modifier.padding(contentPadding)
        // TODO: Remove return later
        val topContent = contents.firstOrNull() ?: return
        val uriHandler = LocalUriHandler.current
        ContentItem(
            image = ContentImage(topContent.article.imageUrl),
            onClick = { uriHandler.openUri(topContent.article.articleUrl.address) },
            imageLabelContent = {
                CategoryText(
                    topContent.categoryTitle,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            },
            descriptionContent = {
                Spacer(Modifier.height(12.dp))
                topContent.article.description.subhead?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(6.dp))
                }
                Text(
                    text = topContent.article.description.title,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 42.sp
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.studying)
                    )
                    Icon(
                        painterResource(R.drawable.top_left_arrow),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            },
            modifier = contentPaddingModifier
        )
        Spacer(Modifier.height(24.dp))
        FlowRow(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .then(contentPaddingModifier),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            contents.drop(1).forEach { content ->
                key(content.article.articleUrl) {
                    ContentItem(
                        image = ContentImage(content.article.imageUrl),
                        onClick = {
                            uriHandler.openUri(content.article.articleUrl.address)
                        },
                        imageLabelContent = {
                            CategoryText(
                                content.categoryTitle,
                                modifier = Modifier.align(Alignment.TopStart)
                            )
                        },
                        descriptionContent = {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = content.article.description.title,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                            Text(
                                text = content.article.readingTime!!,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        modifier = Modifier
                            .width(240.dp)
                            .fillMaxRowHeight()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LatestContentsSection(
    contents: List<Content>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFfbf9f7))
            .padding(vertical = 24.dp)
    ) {
        val contentPaddingModifier = Modifier.padding(contentPadding)
        val uriHandler = LocalUriHandler.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .clickable(
                    onClick = {
                        uriHandler.openUri("$BASE_URL/news-opinion")
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .then(contentPaddingModifier),
        ) {
            Text(
                text = stringResource(R.string.latest_content),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                painterResource(R.drawable.top_left_arrow),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        FlowRow(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .then(contentPaddingModifier),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            contents.forEach { content ->
                key(content.article.articleUrl) {
                    ContentItem(
                        image = ContentImage(content.article.imageUrl),
                        onClick = {
                            uriHandler.openUri(content.article.articleUrl.address)
                        },
                        imageLabelContent = {
                            CategoryText(
                                content.categoryTitle,
                                modifier = Modifier.align(Alignment.TopStart)
                            )
                        },
                        descriptionContent = {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = content.article.description.title,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                            Text(
                                text = content.article.readingTime!!,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        modifier = Modifier
                            .width(240.dp)
                            .fillMaxRowHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun LatestShortVideosSection(
    shortVideos: List<ShortVideo>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(vertical = 24.dp)
    ) {
        val contentPaddingModifier = Modifier.padding(contentPadding)
        Text(
            stringResource(R.string.latest_short_videos_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = contentPaddingModifier,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.latest_short_videos_description),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = contentPaddingModifier,
        )
        Spacer(Modifier.height(12.dp))
        val uriHandler = LocalUriHandler.current
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = contentPadding
        ) {
            items(shortVideos) {
                ShortVideoItem(
                    posterUrl = it.posterUrl,
                    onClick = {
                        uriHandler.openUri(it.videoUrl.address.also { Log.d("ShortVideo", it) })
                    },
                    duration = it.duration,
                    modifier = Modifier.width(240.dp)
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .clickable(
                    onClick = {
                        uriHandler.openUri("$BASE_URL/short-videos/")
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .then(contentPaddingModifier)
                .padding(bottom = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.more_videos),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelLarge
            )
            Icon(
                painterResource(R.drawable.top_left_arrow),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LatestVideosSection(
    videos: List<Video>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFF1A1A1A))
            .padding(vertical = 24.dp)
    ) {
        val contentPaddingModifier = Modifier.padding(contentPadding)
        val uriHandler = LocalUriHandler.current
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(contentPaddingModifier)
            ) {
                Text(
                    stringResource(R.string.nooshdaroo_videos),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                uriHandler.openUri("$BASE_URL/videos")
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.more_videos),
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Icon(
                        painterResource(R.drawable.top_left_arrow),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color.White, modifier = contentPaddingModifier)
            Spacer(Modifier.height(36.dp))
            BoxWithConstraints {
                val animatable = remember { Animatable(0f) }
                Column {
                    var i by remember { mutableIntStateOf(0) }
                    val scrollState = rememberScrollState()
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState, enabled = false)
                            .pointerInput(i) {
                                var direction = 1f
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        if (i == 0 && direction == -1f) {
                                            i = videos.size - 1
                                        } else {
                                            i += direction.toInt()
                                        }
                                    }
                                ) { change, dragAmount ->
                                    change.consume()
                                    direction = dragAmount.sign
                                }
                            },
                    ) {
                        videos.forEach { video ->
                            key(video.videoUrl) {
                                VideoItem(
                                    description = video.description,
                                    posterUrl = video.posterUrl,
                                    onClick = {
                                        uriHandler.openUri(video.videoUrl.address)
                                    },
                                    duration = video.duration,
                                    modifier = Modifier
                                        .width(this@BoxWithConstraints.maxWidth)
                                        .fillMaxRowHeight()
                                        .then(contentPaddingModifier)
                                        .alpha(animatable.value)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                    val currentPage by remember {
                        derivedStateOf {
                            scrollState.viewportSize.takeIf { it != 0 }
                                ?.let { scrollState.value / it } ?: 0f
                        }
                    }
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(videos.size) { iteration ->
                            val color = if (currentPage == iteration) {
                                Color.LightGray
                            } else {
                                Color.DarkGray
                            }
                            val scope = rememberCoroutineScope()
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .padding(horizontal = 2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(12.dp)
                                    .clickable {
                                        scope.launch {
                                            animatable.animateTo(0f)
                                            scrollState.scrollTo(iteration * scrollState.viewportSize)
                                            i = iteration
                                        }
                                    }
                            )
                        }

                        LaunchedEffect(i, videos.isNotEmpty()) {
                            if (videos.isNotEmpty()) {
                                animatable.animateTo(0f)
                                scrollState.scrollTo((i % videos.size) * scrollState.viewportSize)
                                animatable.animateTo(1f)
                                delay(6.seconds)
                                i++
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LatestDigitalLiteracyContentSection(
    articles: List<Article>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(0xFFF2F2F2))
            .padding(vertical = 24.dp)
    ) {
        val contentPaddingModifier = Modifier.padding(contentPadding)
        Image(
            painterResource(R.drawable.cotton),
            contentDescription = null,
            modifier = Modifier.size(108.dp, 56.dp)
        )
        Spacer(Modifier.height(8.dp))
        val uriHandler = LocalUriHandler.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(contentPaddingModifier),
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clickable(
                        onClick = {
                            uriHandler.openUri("$BASE_URL/digital-literacy")
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
            ) {
                Text(
                    text = stringResource(R.string.digital_literacy),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    painterResource(R.drawable.top_left_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .then(contentPaddingModifier)
        ) {
            articles.forEach {
                key(it.articleUrl) {
                    ContentItem(
                        image = ContentImage(it.imageUrl),
                        onClick = {
                            uriHandler.openUri(it.articleUrl.address)
                        },
                        descriptionContent = {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = it.description.title,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                it.description.subhead!!,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        modifier = Modifier
                            .width(240.dp)
                            .fillMaxRowHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyNoteSection(
    note: String,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .background(Color(0xFF1A1A1A))
            .padding(contentPadding)
            .padding(top = 24.dp)
    ) {
        CategoryText(stringResource(R.string.daily_note))
        Text(
            text = stringResource(R.string.daily_note_quote, note),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Image(
            painterResource(R.drawable.presentation),
            contentDescription = null,
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LatestInParentalControlSection(
    articles: List<Article>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val contentPaddingModifier = Modifier.padding(contentPadding)
        Image(
            painterResource(R.drawable.cotton),
            contentDescription = null,
            modifier = Modifier.size(108.dp, 56.dp)
        )
        Spacer(Modifier.height(8.dp))
        val uriHandler = LocalUriHandler.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(contentPaddingModifier),
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clickable(
                        onClick = {
                            uriHandler.openUri("$BASE_URL/parental-control")
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
            ) {
                Text(
                    text = stringResource(R.string.parental_control),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    painterResource(R.drawable.top_left_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        BoxWithConstraints {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = contentPaddingModifier
            ) {
                articles.forEach {
                    ContentItem(
                        image = ContentImage(it.imageUrl),
                        onClick = {
                            uriHandler.openUri(it.articleUrl.address)
                        },
                        descriptionContent = {
                            Column(
                                modifier = Modifier
                                    .background(color = Color(0xFFF2F2F2))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = it.description.title,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                Text(
                                    text = it.readingTime!!,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxRowHeight()
                            .width(this@BoxWithConstraints.maxWidth / 2 - (8.dp + 16.dp))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PopularContentsSection(
    contents: List<Content>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .background(Color(0xFF1A1A1A))
            .padding(vertical = 24.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            val contentPaddingModifier = Modifier.padding(contentPadding)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(contentPaddingModifier),
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.popular_contents),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            val uriHandler = LocalUriHandler.current
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .then(contentPaddingModifier)
            ) {
                contents.forEach {
                    key(it.article.articleUrl) {
                        Column(modifier = Modifier
                            .fillMaxRowHeight()
                            .width(240.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .height(24.dp)
                            ) {
                                Image(
                                    painterResource(R.drawable.small_logo),
                                    contentDescription = null,
                                    modifier = Modifier.aspectRatio(1f)
                                )
                                Text(
                                    stringResource(R.string.nooshdaroo),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            ContentItem(
                                image = ContentImage(it.article.imageUrl),
                                onClick = {
                                    uriHandler.openUri(it.article.articleUrl.address)
                                },
                                imageLabelContent = {
                                    CategoryText(
                                        it.categoryTitle,
                                        modifier = Modifier.align(Alignment.TopEnd)
                                    )
                                },
                                descriptionContent = {
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = it.article.description.title,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = it.article.description.subhead!!,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Light,
                                        maxLines = 3,
                                        minLines = 3
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                    Text(
                                        text = it.article.readingTime!!,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmergencyArticlesSection(
    articles: List<Article>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFF4F1EC))
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val contentPaddingModifier = Modifier.padding(contentPadding)
        Image(
            painterResource(R.drawable.cotton),
            contentDescription = null,
            modifier = Modifier.size(108.dp, 56.dp)
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(contentPaddingModifier),
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.emergency_articles),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        val uriHandler = LocalUriHandler.current
        BoxWithConstraints {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = contentPaddingModifier
            ) {
                articles.forEach {
                    ContentItem(
                        image = ContentImage(it.imageUrl),
                        onClick = {
                            uriHandler.openUri(it.articleUrl.address)
                        },
                        imageLabelContent = {
                            Text(
                                it.description.title,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.4f))
                                    .fillMaxSize()
                                    .padding(8.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxRowHeight()
                            .width(this@BoxWithConstraints.maxWidth / 2 - (8.dp + 16.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun AboutNooshdarooSection(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .background(Color.White)
            .padding(contentPadding)
            .padding(top = 24.dp)
    ) {
        Icon(
            painterResource(R.drawable.ic_megaphone),
            contentDescription = null,
            tint = Color(0xFFBFB697)
        )
        Text(
            stringResource(R.string.where_is_nooshdaroo),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider()
        Text(
            stringResource(R.string.nooshdaroo_about_description),
            fontWeight = FontWeight.Light
        )
        Image(
            painterResource(R.drawable.presentation),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.9f), BlendMode.Lighten),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    PreviewNooshdarooTheme {
        MainScreen(
            uiState = MainUiState(
                categories = listOf(
                    Category(
                        "همه‌ی مطالب",
                        Url("http://localhost:63210/all")
                    ),
                    Category(
                        "بحران و شرایط اضطراری",
                        Url("http://localhost:63210/crisis-and-war/")
                    ),
                    Category(
                        "خبر و تحلیل",
                        Url("http://localhost:63210/news-opinion/")
                    ),
                    Category(
                        "مبانی امنیت",
                        Url("http://localhost:63210/cybersecurity-basics/")
                    ),
                    Category(
                        "حریم خصوصی",
                        Url("http://localhost:63210/privacy-protection/")
                    ),
                    Category(
                        "انواع کلاهبرداری",
                        Url("http://localhost:63210/fraud-awareness/")
                    ),
                    Category(
                        "امنیت در تلفن هوشمند",
                        Url("http://localhost:63210/smartphone-security/")
                    ),
                    Category(
                        "خرید آنلاین",
                        Url("http://localhost:63210/secure-online-shopping/")
                    ),
                    Category(
                        "فروش آنلاین",
                        Url("http://localhost:63210/safe-ecommerce-selling/")
                    ),
                    Category(
                        "معاملات دست دوم",
                        Url("http://localhost:63210/safe-secondhand-deals/")
                    ),
                    Category(
                        "ابزارها و افزونه‌ها",
                        Url("http://localhost:63210/security-tools/")
                    ),
                    Category(
                        "راهنمای والدین",
                        Url("http://localhost:63210/parental-control/")
                    ),
                    Category(
                        "ارزهای دیجیتال",
                        Url("http://localhost:63210/crypto-security/")
                    ),
                    Category(
                        "راهنماهای قانونی",
                        Url("http://localhost:63210/legal-guides/")
                    ),
                    Category(
                        "سواد دیجیتال",
                        Url("http://localhost:63210/digital-literacy/")
                    )
                ),
                featuredContents = listOf(
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da04ce3c3ec.webp"),
                            description = Description(
                                title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی‌ها آغاز شد",
                                subhead = "مراقب دارایی خود باشید!"
                            ),
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/dai-to-usds-auto-conversion-warning/")
                        )
                    ),
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da12866fd3b-1024x576.webp"),
                            description = Description(
                                title = "کره جنوبی دسترسی به «اینترنت پایه» را برای شهروندان رایگان می‌کند"
                            ),
                            readingTime = "زمان مطالعه ۲ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/south-korea-universal-internet-access/")
                        )
                    ),
                    Content(
                        "راهنماهای قانونی",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d96ff009fad-1024x576.webp"),
                            description = Description(
                                title = "آشنایی با «هوش مصنوعی سایه»: وقتی کارمندان قوانین امنیتی را دور می‌زنند!"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/legal-guides/shadow-ai-security-risks/")
                        )
                    ),
                    Content(
                        "امنیت در تلفن هوشمند",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d92187b1081-1024x576.webp"),
                            description = Description(
                                title = "چگونه FBI پیام‌های حذف‌شده‌ی پیام‌رسان سیگنال را از آیفون استخراج کرد؟"
                            ),
                            readingTime = "زمان مطالعه ۳ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/smartphone-security/fbi-accessed-signal-messages-via-ios/")
                        )
                    ),
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d8c73a852e8-1024x576.webp"),
                            description = Description(
                                title = "دروغ بزرگ واتساپ؛ شاید پیام‌های شما هرگز خصوصی نبوده‌اند!"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/whatsapp-under-fire-for-privacy-practices/")
                        )
                    ),
                    Content(
                        "مبانی امنیت",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d75b1d512fc-1024x576.webp"),
                            description = Description(
                                title = "آیا بیت‌لاکر واقعاً امن است؟ وقتی کلیدهای رمزنگاری به دست FBI می‌رسد"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/cybersecurity-basics/is-bitlocker-safe/")
                        )
                    ),
                    Content(
                        "حریم خصوصی",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d0dc3dbe019-1024x576.webp"),
                            description = Description(
                                title = "خطرات داده بیومتریک در عصر دیجیتال: بدن شما چگونه حریم خصوصی‌تان را لو می‌دهد؟"
                            ),
                            readingTime = "زمان مطالعه ۷ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/privacy-protection/biometric-data-risks/")
                        )
                    ),
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d68635763e3-1024x576.webp"),
                            description = Description(
                                title = "هوش مصنوعی جدید آنتروپیک هزاران آسیب‌پذیری در معتبرترین سیستم‌ها پیدا کرد!"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/anthropic-claude-mythos-glasswing/")
                        )
                    ),
                    Content(
                        "سواد دیجیتال",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d3fac56689d-1024x576.webp"),
                            description = Description(
                                title = "چرا گوگل، متا و مایکروسافت در حال ساخت نیروگاه‌های گازی هستند؟"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/digital-literacy/ai-data-centers-natural-gas/")
                        )
                    ),
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d350ee31650-1024x576.webp"),
                            description = Description(
                                title = "وقتی هوش مصنوعی برای محافظت از هم‌نوع خود، در برابر انسان‌ می‌ایستد!"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/ai-defying-human-commands/")
                        )
                    ),
                    Content(
                        "بحران و شرایط اضطراری",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d55074dba6e-1024x576.webp"),
                            description = Description(
                                title = "خطوط جایگزین برای تماس با اورژانس «تهران» در شرایط اضطراری اعلام شد"
                            ),
                            readingTime = "زمان مطالعه ۱ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/tehran-emergency-alternative-lines/")
                        )
                    ),
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d258577b258-1024x576.webp"),
                            description = Description(
                                title = "سایه سنگین قطعی اینترنت بر بازار کریپتو: سقوط ۸۰ درصدی معاملات در ایران"
                            ),
                            readingTime = "زمان مطالعه ۲ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/iran-crypto-ecosystem-collapsing/")
                        )
                    ),
                    Content(
                        "حریم خصوصی",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d117890f221-1024x576.webp"),
                            description = Description(
                                title = "رسوایی بزرگ لینکدین و مایکروسافت: آیا کامپیوتر کاربران بدون اجازه تفتیش می‌شود؟!"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/privacy-protection/linkedin-fairlinked-leak/")
                        )
                    )
                ),
                latestContents = listOf(
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da04ce3c3ec.webp"),
                            description = Description(
                                title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی‌ها آغاز شد"
                            ),
                            readingTime = "زمان مطالعه ۲ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/dai-to-usds-auto-conversion-warning/")
                        )
                    ),
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da12866fd3b.webp"),
                            description = Description(
                                title = "کره جنوبی دسترسی به «اینترنت پایه» را برای شهروندان رایگان می‌کند"
                            ),
                            readingTime = "زمان مطالعه ۲ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/south-korea-universal-internet-access/")
                        )
                    ),
                    Content(
                        "خبر و تحلیل",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d92bf5b6773.webp"),
                            description = Description(
                                title = "زنگ خطر برای کاربران ادوبی ریدر؛ کشف آسیب‌پذیری خطرناک در فایل‌های PDF ناشناس"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/news-opinion/adobe-reader-pdf-zero-day-vulnerability/")
                        )
                    )
                ),
                latestShortVideos = listOf(
                    ShortVideo(
                        duration = "۰۰:۵۸",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d74ae42a21e.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/scam-alert-non-existent-sim-cards/")
                    ),
                    ShortVideo(
                        duration = "۰۱:۳۶",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69b96177d4ed9.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/crypto-security/crypto-frauds-while-disconnecting-internet/")
                    ),
                    ShortVideo(
                        duration = "۰۱:۵۵",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69afc29ccdae1.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/protection-digital-currency-war/")
                    ),
                    ShortVideo(
                        duration = "۰۱:۳۶",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_69a2ad9beb197.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/protect-identity-in-crisis/")
                    ),
                    ShortVideo(
                        duration = "۰۲:۴۵",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b6176b052.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/power-water-outage-essential-needs/")
                    ),
                    ShortVideo(
                        duration = "۰۱:۰۱",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69a53e982c869.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/%d8%aa%d9%86%d8%b8%db%8c%d9%85%d8%a7%d8%aa-%d9%85%d9%87%d9%85-%d8%b3%db%8c%d8%b1%db%8c-%da%a9%d9%87-%d8%b4%d8%a7%db%8c%d8%af-%db%8c%da%a9-%d8%b1%d9%88%d8%b2-%d8%ac%d8%a7%d9%86%d8%aa%d8%a7%d9%86-%d8%b1/")
                    ),
                    ShortVideo(
                        duration = "۰۲:۲۳",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b0d48e5a2.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/fix-site-loading-digital-privacy/")
                    ),
                    ShortVideo(
                        duration = "۰۱:۲۷",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_698b22df629c3.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/dns-security-guide/")
                    ),
                    ShortVideo(
                        duration = "۰۱:۱۲",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6972370b71e18-576x1024.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/security-tools/what-is-nexcloud/")
                    ),
                    ShortVideo(
                        duration = "۰۰:۵۷",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_69690bea5b429.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/smartphone-security/malware-or-international-internet/")
                    ),
                    ShortVideo(
                        duration = "null",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_68550a04e843f.webp"),
                        videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/super-power-saving-in-iphone/")
                    )
                ),
                latestVideos = listOf(
                    Video(
                        videoUrl = Url("https://nooshdaroo.ir/videos/crisis-and-war/using-tourniquet-to-stop-bleeding/"),
                        description = Description(
                            title = "نحوه کنترل خونریزی شدید دست و پا در محل حادثه (کمک‌های اولیه)",
                            subhead = ""
                        ),
                        duration = "۰۳:۳۶",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_6857c77c2a9b5.webp")
                    ),
                    Video(
                        videoUrl = Url("https://nooshdaroo.ir/videos/crisis-and-war/first-aid-in-first-minitue/"),
                        description = Description(
                            title = "کمک‌های اولیه در نخستین لحظات حادثه",
                            subhead = ""
                        ),
                        duration = "۰۳:۳۴",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_685651e38fea6.webp")
                    ),
                    Video(
                        videoUrl = Url("https://nooshdaroo.ir/videos/smartphone-security/7-essential-telegram-security/"),
                        description = Description(
                            title = "هفت تنظیم ضروری تلگرام برای امنیت و حفاظت از حریم شخصی",
                            subhead = "تلگرام به همان اندازه که می‌تواند ابزار امنی برای مکالمه با دیگران باشد، می‌تواند ابزاری باشد برای لو دادن اطلاعات خصوصی و فعالیت‌هایمان! بنابراین ضروری"
                        ),
                        duration = "۰۵:۳۵",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6988724958faf.webp")
                    ),
                    Video(
                        videoUrl = Url("https://nooshdaroo.ir/videos/fraud-awareness/sex-scam-shame-trap/"),
                        description = Description(
                            title = "کلاه‌برداری صیغه و رابطه جنسی: وقتی کلاه‌بردار روی حس شرم دست می‌گذارد!",
                            subhead = "با اندکی جست‌وجو در تلگرام و اینستاگرام، می‌توان با انبوهی از کانال‌ها و صفحات روبه‌رو شد که وعده «صیغه» و «ماساژ» می‌دهند، اما در واقع"
                        ),
                        duration = "۰۲:۰۳",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/10/nooshdaroo_68f338f6beb85.webp")
                    ),
                    Video(
                        videoUrl = Url("https://nooshdaroo.ir/videos/legal-guides/report-scam-and-get-refund/"),
                        description = Description(
                            title = "از گزارش تخلف تا بازگرداندن پول در پلتفرم‌های ثبت آگهی: چگونه از کلاه‌برداران شکایت کنیم؟",
                            subhead = "هنگامی‌ که در یک پلتفرم ثبت آگهی مورد کلاهبرداری قرار می‌گیریم، لازم است مجموعه‌ای از مراحل مشخص را طی کنیم تا شانس واقعی برای بازگرداندن"
                        ),
                        duration = "۰۳:۰۵",
                        posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/10/nooshdaroo_68e624431bf5a.webp")
                    )
                ),
                latestDigitalLiteracyArticles = listOf(
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d96ff009fad-768x432.webp"),
                        description = Description(
                            subhead = "میان‌برهای پرهزینه …",
                            title = "آشنایی با «هوش مصنوعی سایه»: وقتی کارمندان قوانین امنیتی را دور می‌زنند!"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/legal-guides/shadow-ai-security-risks/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d3fac56689d-768x432.webp"),
                        description = Description(
                            subhead = "بحران انرژی در راه است؟",
                            title = "چرا گوگل، متا و مایکروسافت در حال ساخت نیروگاه‌های گازی هستند؟"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/digital-literacy/ai-data-centers-natural-gas/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d350ee31650-768x432.webp"),
                        description = Description(
                            subhead = "روی تاریک و پیش‌بینی‌نشده‌ی هوش مصنوعی",
                            title = "وقتی هوش مصنوعی برای محافظت از هم‌نوع خود، در برابر انسان‌ می‌ایستد!"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/ai-defying-human-commands/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d0dc98b0cbe-768x432.webp"),
                        description = Description(
                            subhead = "انقلاب جدید گوگل در روزهای خاموشی اینترنت ایران!",
                            title = "گوگل Gemma 4 را معرفی کرد؛ مدل‌های متن‌باز رایگان با امکان اجرای آفلاین"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/google-gemma4/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c9102153f3d-768x432.webp"),
                        description = Description(
                            subhead = "«توجه! توجه! توجه!»",
                            title = "درباره ایستگاه رادیویی مرموزی که همزمان با جنگ شروع به پخش کرد چه می‌دانیم؟"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/digital-literacy/numbers-station-during-iran-usa-war/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c46c12443ce-768x432.webp"),
                        description = Description(
                            subhead = "کرک‌هایی خطرناک‌تر از همیشه",
                            title = "توصیه به گیمرها: به‌هیچ‌وجه سراغ نسل جدید بازی‌های کرکی (Hypervisor) نروید"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/parental-control/hypervisor-cracks-dangers/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c8fd8b65b-768x432.avif"),
                        description = Description(
                            subhead = "شبکه‌ای که می‌بیند و حس می‌کند …",
                            title = "آشنایی با شبکه 6G: نسل بعدی اینترنت موبایل"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/digital-literacy/6g-explained/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c0655dfc2-768x432.webp"),
                        description = Description(
                            subhead = "مشکلِ قابل ‌پیش‌بینی بودن!",
                            title = "چرا پسوردهای ساده خیلی راحت فاش می‌شوند؟"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/digital-literacy/why-simple-passwords-get-hacked-easily/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3a24690722-768x432.webp"),
                        description = Description(
                            subhead = "اتفاقی آنقدر عادی که دیگر برایمان عجیب نیست!",
                            title = "معجزه در کف اقیانوس: اطلاعاتِ اینترنتی چطور در کسری از ثانیه جابه‌جا می‌شوند؟"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/digital-literacy/how-submarine-cables-work/")
                    )
                ),
                dailyNote = "دیوار خانه\u200Cی بی\u200Cدر و پیکر، طعمهٔ رهگذران است؛ سیستم بی\u200Cفایروال، بدتر!",
                latestParentalControl = listOf(
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c46c12443ce-300x169.webp"),
                        description = Description(
                            title = "توصیه به گیمرها: به‌هیچ‌وجه سراغ نسل جدید بازی‌های کرکی (Hypervisor) نروید"
                        ),
                        readingTime = "زمان مطالعه ۶ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/parental-control/hypervisor-cracks-dangers/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_69957fa3ad211-300x169.webp"),
                        description = Description(
                            title = "راهکارهای عملی: چطور در شرایط بحران و جنگ، آرامش فرزندمان را حفظ کنیم؟"
                        ),
                        readingTime = "زمان مطالعه ۷ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/child-peace-in-crisis/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_698395cf05cd9-300x169.webp"),
                        description = Description(
                            title = "چرا کودکان امروزی مضطرب‌ترند؟ ردپای تکنولوژی در سلامت روان"
                        ),
                        readingTime = "زمان مطالعه ۸ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/parental-control/childhood-stress/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6967e3e48634f-300x169.webp"),
                        description = Description(
                            title = "راه‌های کاهش استرس کودک در شرایط بحرانی؛ توصیه‌های مهم برای والدین"
                        ),
                        readingTime = "زمان مطالعه ۷ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/reducing-child-stress-in-crisis/")
                    )
                ),
                emergencyArticles = listOf(
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d55074dba6e-300x169.webp"),
                        description = Description(
                            title = "خطوط جایگزین برای تماس با اورژانس «تهران» در شرایط اضطراری اعلام شد"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/tehran-emergency-alternative-lines/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d4d98915b14-300x169.webp"),
                        description = Description(
                            title = "چطور لباس‌ها را با کم‌ترین مقدار آب بشوییم و بهداشت فردی را حفظ کنیم؟"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/washing-clothes-with-less-water/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/08/nooshdaroo_68a310cb05bdc-300x169.webp"),
                        description = Description(
                            title = "هفت نشانه‌ی کلاهبرداری ارز دیجیتال در زمان قطعی اینترنت"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/crypto-security/crypto-scam-signs-in-crisis/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69ba9e321f576-300x169.webp"),
                        description = Description(
                            title = "۶ راهکار برای بازگردانی امنیت روانی به محیط خانه"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/mental-peace-at-home/")
                    )
                ),
                popularContents = listOf(
                    Content(
                        "بحران و شرایط اضطراری",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6964e0c11c4ef.webp"),
                            description = Description(
                                title = "لینک دسترسی به برخی سایت‌‌ها برای دوران قطعی اینترنت",
                                subhead = "در شرایطی که کل اینترنت از دسترس خارج شده\u200C، متاسفانه لازم است لیستی از سایت\u200Cهای ضروری را دم دست داشته باشیم …"
                            ),
                            readingTime = "زمان مطالعه ۵ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/access-list/")
                        )
                    ),
                    Content(
                        "بحران و شرایط اضطراری",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_699aeed4d2032.webp"),
                            description = Description(
                                title = "راهنمای نگهداری گوشت در بحران و بی‌برقی",
                                subhead = "وقتی شرایط بحرانی است و برق برای مدتی طولانی قطع می\u200Cشود، فریزر کارایی\u200C\u200Cاش را از دست می\u200Cدهد و مرغ و گوشت شما به سرعت فاسد می\u200Cشود …"
                            ),
                            readingTime = "زمان مطالعه ۱۰ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/meat-preservation-in-crisis/")
                        )
                    ),
                    Content(
                        "بحران و شرایط اضطراری",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_685101130d31c.webp"),
                            description = Description(
                                title = "۱۰ نکته برای مصرف بنزین کمتر هنگام رانندگی",
                                subhead = "با رعایت برخی نکات ساده\u200Cی رانندگی، می\u200Cتوان تأثیر قابل\u200Cتوجهی بر مصرف سوخت و سلامت فنی خودرو گذاشت. اینجا به ده مورد از مهم\u200Cترین آنها می\u200Cپردازیم."
                            ),
                            readingTime = "زمان مطالعه ۳ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/10-fuel-efficient-driving-techniques/")
                        )
                    ),
                    Content(
                        "بحران و شرایط اضطراری",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69b9bdabed901.webp"),
                            description = Description(
                                title = "بعد از شکستن شیشه‌ها بر اثر موج انفجار چه کنیم؟",
                                subhead = "حتی اگر به پنجره چسب زده باشیم، انفجار می\u200Cتواند آنقدر مهیب باشد که شیشه\u200Cها هزار تکه شوند. در این موقعیت چه کنیم؟"
                            ),
                            readingTime = "زمان مطالعه ۴ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/what-to-do-after-blast/")
                        )
                    ),
                    Content(
                        "بحران و شرایط اضطراری",
                        Article(
                            imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69aeeac7477d5.webp"),
                            description = Description(
                                title = "کاهش خطر پرتاب شیشه در لحظه انفجار: چه کنیم و چه نکنیم؟",
                                subhead = "هنگام انفجار، بیشترین خطر برای ساکنان خانه\u200Cها نه فرو ریختن ساختمان بلکه پرتاب خرده\u200Cشیشه\u200Cها و کنده شدن درها بر اثر موج انفجار است."
                            ),
                            readingTime = "زمان مطالعه ۵ دقیقه",
                            articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/window-safety-during-explosion/")
                        )
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainTopBarPreview() {
    PreviewNooshdarooTheme {
        MainTopBar()
    }
}

@Preview
@Composable
private fun CategoriesRowPreview() {
    PreviewNooshdarooTheme {
        CategoriesRow(
            categories = listOf(
                Category(
                    "همه‌ی مطالب",
                    Url("http://localhost:63210/all")
                ),
                Category(
                    "بحران و شرایط اضطراری",
                    Url("http://localhost:63210/crisis-and-war/")
                ),
                Category(
                    "خبر و تحلیل",
                    Url("http://localhost:63210/news-opinion/")
                ),
                Category(
                    "مبانی امنیت",
                    Url("http://localhost:63210/cybersecurity-basics/")
                ),
                Category(
                    "حریم خصوصی",
                    Url("http://localhost:63210/privacy-protection/")
                ),
                Category(
                    "انواع کلاهبرداری",
                    Url("http://localhost:63210/fraud-awareness/")
                ),
                Category(
                    "امنیت در تلفن هوشمند",
                    Url("http://localhost:63210/smartphone-security/")
                ),
                Category(
                    "خرید آنلاین",
                    Url("http://localhost:63210/secure-online-shopping/")
                ),
                Category(
                    "فروش آنلاین",
                    Url("http://localhost:63210/safe-ecommerce-selling/")
                ),
                Category(
                    "معاملات دست دوم",
                    Url("http://localhost:63210/safe-secondhand-deals/")
                ),
                Category(
                    "ابزارها و افزونه‌ها",
                    Url("http://localhost:63210/security-tools/")
                ),
                Category(
                    "راهنمای والدین",
                    Url("http://localhost:63210/parental-control/")
                ),
                Category(
                    "ارزهای دیجیتال",
                    Url("http://localhost:63210/crypto-security/")
                ),
                Category(
                    "راهنماهای قانونی",
                    Url("http://localhost:63210/legal-guides/")
                ),
                Category(
                    "سواد دیجیتال",
                    Url("http://localhost:63210/digital-literacy/")
                )
            )
        )
    }
}

@Preview
@Composable
private fun FeaturedContentsSectionPreview() {
    PreviewNooshdarooTheme {
        FeaturedContentsSection(
            contentPadding = PaddingValues(horizontal = 16.dp),
            contents = listOf(
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da04ce3c3ec.webp"),
                        description = Description(
                            title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی‌ها آغاز شد",
                            subhead = "مراقب دارایی خود باشید!"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/dai-to-usds-auto-conversion-warning/")
                    )
                ),
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da12866fd3b-1024x576.webp"),
                        description = Description(
                            title = "کره جنوبی دسترسی به «اینترنت پایه» را برای شهروندان رایگان می‌کند"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/south-korea-universal-internet-access/")
                    )
                ),
                Content(
                    "راهنماهای قانونی",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d96ff009fad-1024x576.webp"),
                        description = Description(
                            title = "آشنایی با «هوش مصنوعی سایه»: وقتی کارمندان قوانین امنیتی را دور می‌زنند!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/legal-guides/shadow-ai-security-risks/")
                    )
                ),
                Content(
                    "امنیت در تلفن هوشمند",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d92187b1081-1024x576.webp"),
                        description = Description(
                            title = "چگونه FBI پیام‌های حذف‌شده‌ی پیام‌رسان سیگنال را از آیفون استخراج کرد؟"
                        ),
                        readingTime = "زمان مطالعه ۳ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/smartphone-security/fbi-accessed-signal-messages-via-ios/")
                    )
                ),
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d8c73a852e8-1024x576.webp"),
                        description = Description(
                            title = "دروغ بزرگ واتساپ؛ شاید پیام‌های شما هرگز خصوصی نبوده‌اند!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/whatsapp-under-fire-for-privacy-practices/")
                    )
                ),
                Content(
                    "مبانی امنیت",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d75b1d512fc-1024x576.webp"),
                        description = Description(
                            title = "آیا بیت‌لاکر واقعاً امن است؟ وقتی کلیدهای رمزنگاری به دست FBI می‌رسد"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/cybersecurity-basics/is-bitlocker-safe/")
                    )
                ),
                Content(
                    "حریم خصوصی",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d0dc3dbe019-1024x576.webp"),
                        description = Description(
                            title = "خطرات داده بیومتریک در عصر دیجیتال: بدن شما چگونه حریم خصوصی‌تان را لو می‌دهد؟"
                        ),
                        readingTime = "زمان مطالعه ۷ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/privacy-protection/biometric-data-risks/")
                    )
                ),
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d68635763e3-1024x576.webp"),
                        description = Description(
                            title = "هوش مصنوعی جدید آنتروپیک هزاران آسیب‌پذیری در معتبرترین سیستم‌ها پیدا کرد!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/anthropic-claude-mythos-glasswing/")
                    )
                ),
                Content(
                    "سواد دیجیتال",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d3fac56689d-1024x576.webp"),
                        description = Description(
                            title = "چرا گوگل، متا و مایکروسافت در حال ساخت نیروگاه‌های گازی هستند؟"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/digital-literacy/ai-data-centers-natural-gas/")
                    )
                ),
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d350ee31650-1024x576.webp"),
                        description = Description(
                            title = "وقتی هوش مصنوعی برای محافظت از هم‌نوع خود، در برابر انسان‌ می‌ایستد!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/ai-defying-human-commands/")
                    )
                ),
                Content(
                    "بحران و شرایط اضطراری",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d55074dba6e-1024x576.webp"),
                        description = Description(
                            title = "خطوط جایگزین برای تماس با اورژانس «تهران» در شرایط اضطراری اعلام شد"
                        ),
                        readingTime = "زمان مطالعه ۱ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/tehran-emergency-alternative-lines/")
                    )
                ),
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d258577b258-1024x576.webp"),
                        description = Description(
                            title = "سایه سنگین قطعی اینترنت بر بازار کریپتو: سقوط ۸۰ درصدی معاملات در ایران"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/iran-crypto-ecosystem-collapsing/")
                    )
                ),
                Content(
                    "حریم خصوصی",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d117890f221-1024x576.webp"),
                        description = Description(
                            title = "رسوایی بزرگ لینکدین و مایکروسافت: آیا کامپیوتر کاربران بدون اجازه تفتیش می‌شود؟!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/privacy-protection/linkedin-fairlinked-leak/")
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun LatestShortVideosSectionPreview() {
    PreviewNooshdarooTheme {
        LatestShortVideosSection(
            contentPadding = PaddingValues(horizontal = 16.dp),
            shortVideos = listOf(
                ShortVideo(
                    duration = "۰۰:۵۸",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d74ae42a21e.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/scam-alert-non-existent-sim-cards/")
                ),
                ShortVideo(
                    duration = "۰۱:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69b96177d4ed9.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crypto-security/crypto-frauds-while-disconnecting-internet/")
                ),
                ShortVideo(
                    duration = "۰۱:۵۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69afc29ccdae1.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/protection-digital-currency-war/")
                ),
                ShortVideo(
                    duration = "۰۱:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_69a2ad9beb197.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/protect-identity-in-crisis/")
                ),
                ShortVideo(
                    duration = "۰۲:۴۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b6176b052.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/power-water-outage-essential-needs/")
                ),
                ShortVideo(
                    duration = "۰۱:۰۱",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69a53e982c869.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/%d8%aa%d9%86%d8%b8%db%8c%d9%85%d8%a7%d8%aa-%d9%85%d9%87%d9%85-%d8%b3%db%8c%d8%b1%db%8c-%da%a9%d9%87-%d8%b4%d8%a7%db%8c%d8%af-%db%8c%da%a9-%d8%b1%d9%88%d8%b2-%d8%ac%d8%a7%d9%86%d8%aa%d8%a7%d9%86-%d8%b1/")
                ),
                ShortVideo(
                    duration = "۰۲:۲۳",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b0d48e5a2.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/fix-site-loading-digital-privacy/")
                ),
                ShortVideo(
                    duration = "۰۱:۲۷",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_698b22df629c3.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/dns-security-guide/")
                ),
                ShortVideo(
                    duration = "۰۱:۱۲",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6972370b71e18-576x1024.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/security-tools/what-is-nexcloud/")
                ),
                ShortVideo(
                    duration = "۰۰:۵۷",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_69690bea5b429.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/smartphone-security/malware-or-international-internet/")
                ),
                ShortVideo(
                    duration = "null",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_68550a04e843f.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/super-power-saving-in-iphone/")
                )
            )
        )
    }
}

@Preview
@Composable
private fun LatestContentsSectionPreview() {
    PreviewNooshdarooTheme {
        LatestContentsSection(
            contentPadding = PaddingValues(horizontal = 16.dp),
            contents = listOf(
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da04ce3c3ec.webp"),
                        description = Description(
                            title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی‌ها آغاز شد"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/dai-to-usds-auto-conversion-warning/")
                    )
                ),
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da12866fd3b.webp"),
                        description = Description(
                            title = "کره جنوبی دسترسی به «اینترنت پایه» را برای شهروندان رایگان می‌کند"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/south-korea-universal-internet-access/")
                    )
                ),
                Content(
                    "خبر و تحلیل",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d92bf5b6773.webp"),
                        description = Description(
                            title = "زنگ خطر برای کاربران ادوبی ریدر؛ کشف آسیب‌پذیری خطرناک در فایل‌های PDF ناشناس"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/adobe-reader-pdf-zero-day-vulnerability/")
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun LatestVideosSectionPreview() {
    PreviewNooshdarooTheme {
        LatestVideosSection(
            videos = listOf(
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/crisis-and-war/using-tourniquet-to-stop-bleeding/"),
                    description = Description(
                        title = "نحوه کنترل خونریزی شدید دست و پا در محل حادثه (کمک‌های اولیه)",
                        subhead = ""
                    ),
                    duration = "۰۳:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_6857c77c2a9b5.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/crisis-and-war/first-aid-in-first-minitue/"),
                    description = Description(
                        title = "کمک‌های اولیه در نخستین لحظات حادثه",
                        subhead = ""
                    ),
                    duration = "۰۳:۳۴",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_685651e38fea6.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/smartphone-security/7-essential-telegram-security/"),
                    description = Description(
                        title = "هفت تنظیم ضروری تلگرام برای امنیت و حفاظت از حریم شخصی",
                        subhead = "تلگرام به همان اندازه که می‌تواند ابزار امنی برای مکالمه با دیگران باشد، می‌تواند ابزاری باشد برای لو دادن اطلاعات خصوصی و فعالیت‌هایمان! بنابراین ضروری"
                    ),
                    duration = "۰۵:۳۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6988724958faf.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/fraud-awareness/sex-scam-shame-trap/"),
                    description = Description(
                        title = "کلاه‌برداری صیغه و رابطه جنسی: وقتی کلاه‌بردار روی حس شرم دست می‌گذارد!",
                        subhead = "با اندکی جست‌وجو در تلگرام و اینستاگرام، می‌توان با انبوهی از کانال‌ها و صفحات روبه‌رو شد که وعده «صیغه» و «ماساژ» می‌دهند، اما در واقع"
                    ),
                    duration = "۰۲:۰۳",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/10/nooshdaroo_68f338f6beb85.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/legal-guides/report-scam-and-get-refund/"),
                    description = Description(
                        title = "از گزارش تخلف تا بازگرداندن پول در پلتفرم‌های ثبت آگهی: چگونه از کلاه‌برداران شکایت کنیم؟",
                        subhead = "هنگامی‌ که در یک پلتفرم ثبت آگهی مورد کلاهبرداری قرار می‌گیریم، لازم است مجموعه‌ای از مراحل مشخص را طی کنیم تا شانس واقعی برای بازگرداندن"
                    ),
                    duration = "۰۳:۰۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/10/nooshdaroo_68e624431bf5a.webp")
                )
            ),
            contentPadding = PaddingValues(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun LatestDigitalLiteracyContentSectionPreview() {
    PreviewNooshdarooTheme {
        LatestDigitalLiteracyContentSection(
            contentPadding = PaddingValues(horizontal = 16.dp),
            articles = listOf(
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d96ff009fad-768x432.webp"),
                    description = Description(
                        subhead = "میان‌برهای پرهزینه …",
                        title = "آشنایی با «هوش مصنوعی سایه»: وقتی کارمندان قوانین امنیتی را دور می‌زنند!"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/legal-guides/shadow-ai-security-risks/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d3fac56689d-768x432.webp"),
                    description = Description(
                        subhead = "بحران انرژی در راه است؟",
                        title = "چرا گوگل، متا و مایکروسافت در حال ساخت نیروگاه‌های گازی هستند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/ai-data-centers-natural-gas/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d350ee31650-768x432.webp"),
                    description = Description(
                        subhead = "روی تاریک و پیش‌بینی‌نشده‌ی هوش مصنوعی",
                        title = "وقتی هوش مصنوعی برای محافظت از هم‌نوع خود، در برابر انسان‌ می‌ایستد!"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/news-opinion/ai-defying-human-commands/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d0dc98b0cbe-768x432.webp"),
                    description = Description(
                        subhead = "انقلاب جدید گوگل در روزهای خاموشی اینترنت ایران!",
                        title = "گوگل Gemma 4 را معرفی کرد؛ مدل‌های متن‌باز رایگان با امکان اجرای آفلاین"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/news-opinion/google-gemma4/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c9102153f3d-768x432.webp"),
                    description = Description(
                        subhead = "«توجه! توجه! توجه!»",
                        title = "درباره ایستگاه رادیویی مرموزی که همزمان با جنگ شروع به پخش کرد چه می‌دانیم؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/numbers-station-during-iran-usa-war/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c46c12443ce-768x432.webp"),
                    description = Description(
                        subhead = "کرک‌هایی خطرناک‌تر از همیشه",
                        title = "توصیه به گیمرها: به‌هیچ‌وجه سراغ نسل جدید بازی‌های کرکی (Hypervisor) نروید"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/parental-control/hypervisor-cracks-dangers/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c8fd8b65b-768x432.avif"),
                    description = Description(
                        subhead = "شبکه‌ای که می‌بیند و حس می‌کند …",
                        title = "آشنایی با شبکه 6G: نسل بعدی اینترنت موبایل"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/6g-explained/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c0655dfc2-768x432.webp"),
                    description = Description(
                        subhead = "مشکلِ قابل ‌پیش‌بینی بودن!",
                        title = "چرا پسوردهای ساده خیلی راحت فاش می‌شوند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/why-simple-passwords-get-hacked-easily/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3a24690722-768x432.webp"),
                    description = Description(
                        subhead = "اتفاقی آنقدر عادی که دیگر برایمان عجیب نیست!",
                        title = "معجزه در کف اقیانوس: اطلاعاتِ اینترنتی چطور در کسری از ثانیه جابه‌جا می‌شوند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/how-submarine-cables-work/")
                )
            )
        )
    }
}

@Preview
@Composable
private fun DailyNoteSectionPreview() {
    PreviewNooshdarooTheme {
        DailyNoteSection(
            note = "دیوار خانه\u200Cی بی\u200Cدر و پیکر، طعمهٔ رهگذران است؛ سیستم بی\u200Cفایروال، بدتر!",
            contentPadding = PaddingValues(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun LatestInParentalControlSectionPreview() {
    PreviewNooshdarooTheme {
        LatestInParentalControlSection(
            articles = listOf(
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c46c12443ce-300x169.webp"),
                    description = Description(
                        title = "توصیه به گیمرها: به‌هیچ‌وجه سراغ نسل جدید بازی‌های کرکی (Hypervisor) نروید"
                    ),
                    readingTime = "زمان مطالعه ۶ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/parental-control/hypervisor-cracks-dangers/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_69957fa3ad211-300x169.webp"),
                    description = Description(
                        title = "راهکارهای عملی: چطور در شرایط بحران و جنگ، آرامش فرزندمان را حفظ کنیم؟"
                    ),
                    readingTime = "زمان مطالعه ۷ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/child-peace-in-crisis/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_698395cf05cd9-300x169.webp"),
                    description = Description(
                        title = "چرا کودکان امروزی مضطرب‌ترند؟ ردپای تکنولوژی در سلامت روان"
                    ),
                    readingTime = "زمان مطالعه ۸ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/parental-control/childhood-stress/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6967e3e48634f-300x169.webp"),
                    description = Description(
                        title = "راه‌های کاهش استرس کودک در شرایط بحرانی؛ توصیه‌های مهم برای والدین"
                    ),
                    readingTime = "زمان مطالعه ۷ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/reducing-child-stress-in-crisis/")
                )
            ),
            contentPadding = PaddingValues(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun PopularContentsSectionPreview() {
    PreviewNooshdarooTheme {
        PopularContentsSection(
            contents = listOf(
                Content(
                    "بحران و شرایط اضطراری",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6964e0c11c4ef.webp"),
                        description = Description(
                            title = "لینک دسترسی به برخی سایت‌‌ها برای دوران قطعی اینترنت",
                            subhead = "در شرایطی که کل اینترنت از دسترس خارج شده\u200C، متاسفانه لازم است لیستی از سایت\u200Cهای ضروری را دم دست داشته باشیم …"
                        ),
                        readingTime = "زمان مطالعه ۵ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/access-list/")
                    )
                ),
                Content(
                    "بحران و شرایط اضطراری",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_699aeed4d2032.webp"),
                        description = Description(
                            title = "راهنمای نگهداری گوشت در بحران و بی‌برقی",
                            subhead = "وقتی شرایط بحرانی است و برق برای مدتی طولانی قطع می\u200Cشود، فریزر کارایی\u200C\u200Cاش را از دست می\u200Cدهد و مرغ و گوشت شما به سرعت فاسد می\u200Cشود …"
                        ),
                        readingTime = "زمان مطالعه ۱۰ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/meat-preservation-in-crisis/")
                    )
                ),
                Content(
                    "بحران و شرایط اضطراری",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_685101130d31c.webp"),
                        description = Description(
                            title = "۱۰ نکته برای مصرف بنزین کمتر هنگام رانندگی",
                            subhead = "با رعایت برخی نکات ساده\u200Cی رانندگی، می\u200Cتوان تأثیر قابل\u200Cتوجهی بر مصرف سوخت و سلامت فنی خودرو گذاشت. اینجا به ده مورد از مهم\u200Cترین آنها می\u200Cپردازیم."
                        ),
                        readingTime = "زمان مطالعه ۳ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/10-fuel-efficient-driving-techniques/")
                    )
                ),
                Content(
                    "بحران و شرایط اضطراری",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69b9bdabed901.webp"),
                        description = Description(
                            title = "بعد از شکستن شیشه‌ها بر اثر موج انفجار چه کنیم؟",
                            subhead = "حتی اگر به پنجره چسب زده باشیم، انفجار می\u200Cتواند آنقدر مهیب باشد که شیشه\u200Cها هزار تکه شوند. در این موقعیت چه کنیم؟"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/what-to-do-after-blast/")
                    )
                ),
                Content(
                    "بحران و شرایط اضطراری",
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69aeeac7477d5.webp"),
                        description = Description(
                            title = "کاهش خطر پرتاب شیشه در لحظه انفجار: چه کنیم و چه نکنیم؟",
                            subhead = "هنگام انفجار، بیشترین خطر برای ساکنان خانه\u200Cها نه فرو ریختن ساختمان بلکه پرتاب خرده\u200Cشیشه\u200Cها و کنده شدن درها بر اثر موج انفجار است."
                        ),
                        readingTime = "زمان مطالعه ۵ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/window-safety-during-explosion/")
                    )
                )
            ),
            contentPadding = PaddingValues(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun EmergencyArticlesSectionPreview() {
    PreviewNooshdarooTheme {
        EmergencyArticlesSection(
            articles = listOf(
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d55074dba6e-300x169.webp"),
                    description = Description(
                        title = "خطوط جایگزین برای تماس با اورژانس «تهران» در شرایط اضطراری اعلام شد"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/tehran-emergency-alternative-lines/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d4d98915b14-300x169.webp"),
                    description = Description(
                        title = "چطور لباس‌ها را با کم‌ترین مقدار آب بشوییم و بهداشت فردی را حفظ کنیم؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/washing-clothes-with-less-water/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/08/nooshdaroo_68a310cb05bdc-300x169.webp"),
                    description = Description(
                        title = "هفت نشانه‌ی کلاهبرداری ارز دیجیتال در زمان قطعی اینترنت"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crypto-security/crypto-scam-signs-in-crisis/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69ba9e321f576-300x169.webp"),
                    description = Description(
                        title = "۶ راهکار برای بازگردانی امنیت روانی به محیط خانه"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/mental-peace-at-home/")
                )
            ),
            contentPadding = PaddingValues(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun AboutNooshdarooSectionPreview() {
    PreviewNooshdarooTheme {
        AboutNooshdarooSection(
            contentPadding = PaddingValues(horizontal = 16.dp)
        )
    }
}
