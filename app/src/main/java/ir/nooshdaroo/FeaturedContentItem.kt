package ir.nooshdaroo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.nooshdaroo.ui.NooshdarooTheme

data class FeaturedTopContent(
    val categoryTitle: String,
    val imageUrl: Url,
    val subhead: String,
    val title: String,
    val articleUrl: Url
)

data class FeaturedContent(
    val categoryTitle: String,
    val imageUrl: Url,
    val title: String,
    val readingTime: String,
    val articleUrl: Url
)

@Composable
fun FeaturedTopContentItem(
    content: FeaturedTopContent,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = modifier
            .clickable { uriHandler.openUri(content.articleUrl.address) }
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .placeholder(Color.DarkGray.toArgb().toDrawable())
                    .data(content.imageUrl.address)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(2 / 1f)
            )

            Text(
                text = content.categoryTitle,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(4.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = content.subhead,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = content.title,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "مطالعه"
            )
            Icon(
                painterResource(R.drawable.top_left_arrow),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun FeaturedContentItem(
    content: FeaturedContent,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = modifier
            .clickable { uriHandler.openUri(content.articleUrl.address) }
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .placeholder(Color.DarkGray.toArgb().toDrawable())
                    .data(content.imageUrl.address)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.aspectRatio(2 / 1f)
            )

            Text(
                text = content.categoryTitle,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(4.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = content.title,
            minLines = 3,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Text(text = content.readingTime)
    }
}

@Preview
@Composable
private fun FeaturedTopContentItemPreview() {
    NooshdarooTheme {
        FeaturedTopContentItem(
            content = FeaturedTopContent(
                categoryTitle = "خبر و تحلیل",
                imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp"),
                title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی\u200Cها آغاز شد",
                subhead = "خبری عجیب، ترسناک و نویدبخش!",
                articleUrl = Url("https://test.com")
            )
        )
    }
}

@Preview
@Composable
private fun FeaturedContentItemPreview() {
    NooshdarooTheme {
        FeaturedContentItem(
            content = FeaturedContent(
                categoryTitle = "خبر و تحلیل",
                imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp"),
                title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی\u200Cها آغاز شد",
                readingTime = "زمان مطالعه ۴ دقیقه",
                articleUrl = Url("https://test.com")
            )
        )
    }
}
