package ir.nooshdaroo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val desiredLocale = LocaleListCompat.create(Locale.forLanguageTag("fa-IR"))
        AppCompatDelegate.setApplicationLocales(desiredLocale)
    }
}
