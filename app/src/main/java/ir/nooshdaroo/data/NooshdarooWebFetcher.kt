package ir.nooshdaroo.data

interface NooshdarooWebFetcher {
    suspend fun fetchContent(): String
    fun invalidCache()
}
