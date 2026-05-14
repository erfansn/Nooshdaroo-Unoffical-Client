package ir.nooshdaroo.data

import android.content.Context
import android.util.Log
import ir.nooshdaroo.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException

class NooshdarooWebFetcherImpl(context: Context) : NooshdarooWebFetcher {

    private val cacheDir = File(context.cacheDir, "web-fetcher")

    private val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(cacheDir, 1024 * 1024 * 10))
        .build()

    private val requestWithCache = Request.Builder()
        .url(BASE_URL)
        .cacheControl(CacheControl.FORCE_CACHE)
        .build()

    private val request = Request.Builder()
        .url(BASE_URL)
        .build()

    override suspend fun fetchContent(): String {
        return withContext(Dispatchers.IO) {
            try {
                okHttpClient.newCall(request).execute().use {
                    it.body.string()
                }
            } catch (e: IOException) {
                okHttpClient.newCall(requestWithCache).execute().use {
                    if (it.code == 504) {
                        throw UnsatisfiedRequestWithCacheException()
                    } else {
                        it.body.string()
                    }
                }
            }
        }
    }

    override fun invalidCache() {
        cacheDir.delete()
    }

    companion object {
        private const val TAG = "NooshdarooWebFetcherImp"
    }
}

class UnsatisfiedRequestWithCacheException : RuntimeException()
