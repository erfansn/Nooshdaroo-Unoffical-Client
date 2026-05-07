package ir.nooshdaroo

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.platform.UriHandler
import androidx.core.net.toUri

class AppUriHandler(private val context: Context) : UriHandler {

    override fun openUri(uri: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, uri.toUri())
    }
}