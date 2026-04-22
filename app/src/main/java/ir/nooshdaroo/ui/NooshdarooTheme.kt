package ir.nooshdaroo.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.app.LocaleManagerCompat
import androidx.core.content.ContextCompat
import ir.nooshdaroo.R
import org.jetbrains.annotations.TestOnly
import java.util.Locale

private val LightColorScheme = lightColorScheme()

private val Typography = Typography()

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
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            content = content,
            typography = Typography,
            shapes = Shapes,
            colorScheme = LightColorScheme
        )
    }
}
