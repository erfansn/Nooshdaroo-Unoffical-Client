package ir.nooshdaroo

import android.util.Patterns

@JvmInline
value class Url(val address: String) {

    init {
        require(address.startsWith("http://localhost") || Patterns.WEB_URL.matcher(address).matches()) { "Invalid url address $address" }
    }

    val path: String?
        get() {
            var count = 3
            var cIndex = 0
            for ((index, ch) in address.withIndex()) {
                if (ch == '/') {
                    cIndex = index
                    count--
                }
                if (count == 0) break
            }

            return if (count != 0 || cIndex == address.lastIndex) {
                null
            } else {
                address.substring(cIndex).substringBeforeLast("#").substringBeforeLast("?")
            }
        }
}
