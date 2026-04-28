package ir.nooshdaroo.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation.Settings
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import ir.nooshdaroo.R

private val LightColorScheme = lightColorScheme()

@OptIn(ExperimentalTextApi::class)
private val doranFontFamily = FontFamily(
    Font(R.font.doran_vf, FontWeight.Normal, variationSettings = Settings(FontWeight.Normal, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.SemiBold, variationSettings = Settings(FontWeight.SemiBold, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.Black, variationSettings = Settings(FontWeight.Black, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.Bold, variationSettings = Settings(FontWeight.Bold, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.Medium, variationSettings = Settings(FontWeight.Medium, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.Thin, variationSettings = Settings(FontWeight.Thin, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.Light, variationSettings = Settings(FontWeight.Light, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.ExtraBold, variationSettings = Settings(FontWeight.ExtraBold, FontStyle.Normal)),
    Font(R.font.doran_vf, FontWeight.ExtraLight, variationSettings = Settings(FontWeight.ExtraLight, FontStyle.Normal)),
)

private val Typography = with(Typography()) {
    Typography(
        displayLarge = displayLarge.copy(fontFamily = doranFontFamily),
        displayMedium = displayMedium.copy(fontFamily = doranFontFamily),
        displaySmall = displaySmall.copy(fontFamily = doranFontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = doranFontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = doranFontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = doranFontFamily),
        titleLarge = titleLarge.copy(fontFamily = doranFontFamily),
        titleMedium = titleMedium.copy(fontFamily = doranFontFamily),
        titleSmall = titleSmall.copy(fontFamily = doranFontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = doranFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = doranFontFamily),
        bodySmall = bodySmall.copy(fontFamily = doranFontFamily),
        labelLarge = labelLarge.copy(fontFamily = doranFontFamily),
        labelMedium = labelMedium.copy(fontFamily = doranFontFamily),
        labelSmall = labelSmall.copy(fontFamily = doranFontFamily),
    )
}

private val appShape = RoundedCornerShape(0)

private val Shapes = Shapes(
    extraSmall = appShape,
    small = appShape,
    medium = appShape,
    large = appShape,
    extraLarge = appShape
)

@Composable
fun NooshdarooTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content,
        typography = Typography,
        shapes = Shapes,
        colorScheme = LightColorScheme
    )
}

@Composable
fun PreviewNooshdarooTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        NooshdarooTheme(content)
    }
}
