package ir.nooshdaroo.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.nooshdaroo.R
import ir.nooshdaroo.Url
import ir.nooshdaroo.ui.PreviewNooshdarooTheme

@Composable
fun ShortVideoItem(
    posterUrl: Url,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    duration: String? = null
) {
    Box(modifier = modifier.clickable(onClick = onClick)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .placeholder(Color.DarkGray.toArgb().toDrawable())
                .data(posterUrl.address)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1 / 2f)
        )

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
                            painterResource(R.drawable.ic_video),
                            contentDescription = null,
                        )
                    }
                ),
                text = buildAnnotatedString {
                    appendInlineContent("video_icon")
                    append(" ")
                    withStyle(SpanStyle(LocalContentColor.current)) {
                        append(stringResource(R.string.short_video))
                    }
                },
                style = LocalTextStyle.current,
                modifier = Modifier
                    .padding(top = 24.dp, start = 12.dp)
                    .align(Alignment.TopStart)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(4.dp)
                    .padding(horizontal = 2.dp)
            )
        }

        duration?.let {
            Text(
                text = it,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.Black)
                    .padding(horizontal = 6.dp)
            )
        }

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
    }
}

@Preview
@Composable
private fun ShortVideoItemPreview() {
    PreviewNooshdarooTheme {
        ShortVideoItem(
            Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69e8ef3032f01-576x1024.webp"),
            onClick = { },
            duration = "۰۱:۰۰"
        )
    }
}
