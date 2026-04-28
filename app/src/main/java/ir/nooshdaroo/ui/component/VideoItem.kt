package ir.nooshdaroo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.nooshdaroo.R
import ir.nooshdaroo.Url
import ir.nooshdaroo.data.model.Description
import ir.nooshdaroo.ui.PreviewNooshdarooTheme

@Composable
fun VideoItem(
    description: Description,
    posterUrl: Url,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    duration: String? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            BasicText(
                inlineContent = mapOf(
                    "video_icon" to InlineTextContent(
                        Placeholder(
                            16.sp,
                            16.sp,
                            PlaceholderVerticalAlign.Center
                        )
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_video_outlined),
                            contentDescription = null,
                        )
                    }
                ),
                text = buildAnnotatedString {
                    appendInlineContent("video_icon")
                    append(" ")
                    withStyle(SpanStyle(LocalContentColor.current)) {
                        append(stringResource(R.string.educational_video))
                    }
                },
                style = LocalTextStyle.current,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(4.dp)
                    .padding(horizontal = 2.dp)
            )
        }
        Text(
            description.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .placeholder(Color.DarkGray.toArgb().toDrawable())
                    .data(posterUrl.address)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(2 / 1f)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                    .size(44.dp)
            ) {
                Icon(
                    painterResource(R.drawable.ic_play),
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            duration?.let {
                Text(
                    text = it,
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color.Black)
                        .padding(horizontal = 6.dp)
                )
            }
        }
        description.subhead?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Preview
@Composable
private fun VideoItemPreview() {
    PreviewNooshdarooTheme {
        VideoItem(
            description = Description(
                title = "از گزارش تخلف تا بازگرداندن پول در پلتفرم\u200Cهای ثبت آگهی: چگونه از کلاه\u200Cبرداران شکایت کنیم؟",
                subhead = "هنگامی\u200C که در یک پلتفرم ثبت آگهی مورد کلاهبرداری قرار می\u200Cگیریم، لازم است مجموعه\u200Cای از مراحل مشخص را طی کنیم تا شانس واقعی برای بازگرداندن"
            ),
            posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/10/nooshdaroo_68e624431bf5a.webp"),
            onClick = { },
            duration = "۰۳:۰۵"
        )
    }
}
