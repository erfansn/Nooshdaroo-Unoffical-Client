package ir.nooshdaroo

import ir.nooshdaroo.data.NooshdarooWebFetcher

class FakeNooshdarooWebFetcher : NooshdarooWebFetcher {

    override suspend fun fetchContent(): String {
        return javaClass.getResourceAsStream("/index.html")!!.reader().use { it.readText() }
    }

    override fun invalidCache() {
        TODO("Not yet implemented")
    }
}