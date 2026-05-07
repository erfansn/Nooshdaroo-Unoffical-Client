package ir.nooshdaroo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.nooshdaroo.R
import ir.nooshdaroo.Url
import ir.nooshdaroo.ui.PreviewNooshdarooTheme

data class ContentImage(val url: Url, val aspectRatio: Float = 3/2f)

@Composable
fun ContentItem(
    image: ContentImage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageLabelContent: @Composable BoxScope.() -> Unit = { },
    descriptionContent: @Composable ColumnScope.() -> Unit = { }
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .placeholder(Color.DarkGray.toArgb().toDrawable())
                    .data(image.url.address)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.aspectRatio(image.aspectRatio)
            )
            imageLabelContent()
        }
        descriptionContent()
    }
}

@Composable
fun CategoryText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFFfbf8f3),
        modifier = modifier
            .background(color = Color(0xFF895c3e))
            .padding(4.dp),
    )
}

@Preview
@Composable
private fun ContentItemPreview_0() {
    PreviewNooshdarooTheme {
        ContentItem(
            image = ContentImage(Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp")),
            onClick = { },
            imageLabelContent = {
                CategoryText("خبر و تحلیل", modifier = Modifier.align(Alignment.TopStart))
            },
            descriptionContent = {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "راهنمای کاربردی انتقال ارز دیجیتال با حداقل دسترسی به اینترنت",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text(text = "زمان مطالعه ۴ دقیقه")
            },
            modifier = Modifier.height(IntrinsicSize.Min)
        )
    }
}

@Preview
@Composable
private fun ContentItemPreview_1() {
    PreviewNooshdarooTheme {
        ContentItem(
            image = ContentImage(Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp")),
            onClick = { },
            descriptionContent = {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "چرا نباید کنار موبایل خود بخوابید؟ + ۴ راهکار برای ترک این عادت مضر",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "پشت\u200C پرده\u200Cی یک امپراتوری جهانی")
            },
            modifier = Modifier.height(IntrinsicSize.Min)
        )
    }
}

@Preview
@Composable
private fun ContentItemPreview_2() {
    PreviewNooshdarooTheme {
        ContentItem(
            image = ContentImage(Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp")),
            onClick = { },
            descriptionContent = {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "چرا نباید کنار موبایل خود بخوابید؟ + ۴ راهکار برای ترک این عادت مضر",
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "زمان مطالعه ۵ دقیقه")
                }
            },
            modifier = Modifier.height(IntrinsicSize.Min)
        )
    }
}

@Preview
@Composable
private fun ContentItemPreview_3() {
    PreviewNooshdarooTheme {
        ContentItem(
            image = ContentImage(Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp")),
            onClick = { },
            imageLabelContent = {
                CategoryText("بحران و شرایط اضطراری", modifier = Modifier.align(Alignment.TopEnd))
            },
            descriptionContent = {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "چرا نباید کنار موبایل خود بخوابید؟ + ۴ راهکار برای ترک این عادت مضر",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    minLines = 2,
                    maxLines = 2
                )
                Text(
                    text = "وقتی شرایط بحرانی است و برق برای مدتی طولانی قطع می\u200Cشود، فریزر کارایی\u200C\u200Cاش را از دست می\u200Cدهد و مرغ و گوشت شما به سرعت فاسد می\u200Cشود…",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    minLines = 3
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "زمان مطالعه ۵ دقیقه")
            }
        )
    }
}

@Preview
@Composable
private fun ContentItemPreview_4() {
    PreviewNooshdarooTheme {
        ContentItem(
            image = ContentImage(
                Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp"),
                aspectRatio = 3/2f
            ),
            onClick = { },
            imageLabelContent = {
                Text(
                    "راهنمای کاربردی انتقال ارز دیجیتال با حداقل دسترسی به اینترنت",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.4f))
                        .fillMaxSize()
                        .padding(8.dp)
                )
            },
            modifier = Modifier.height(IntrinsicSize.Min)
        )
    }
}

@Preview
@Composable
private fun ContentItemPreview_5() {
    PreviewNooshdarooTheme {
        ContentItem(
            image = ContentImage(Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e7b81ef3b25.webp"),),
            onClick = { },
            imageLabelContent = {
                CategoryText("بحران و شرایط اضطراری", modifier = Modifier.align(Alignment.TopStart))
            },
            descriptionContent = {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "خبری عجیب، ترسناک و نویدبخش!",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی\u200Cها آغاز شد",
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
            modifier = Modifier.height(IntrinsicSize.Min)
        )
    }
}
