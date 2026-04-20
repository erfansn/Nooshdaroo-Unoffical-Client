package ir.nooshdaroo

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UrlTest {

    @Test
    fun returns_path_correctly_when_url_with_long_path_passed() {
        val url = Url("http://localhost:3232/a/b/c")

        assertThat(url.path).isEqualTo("a/b/c")
    }

    @Test
    fun returns_empty_as_path_when_no_path_in_passed_url() {
        val url = Url("http://localhost/")
        val url2 = Url("http://localhost")
        val url3 = Url("http://localhost?param=value")

        assertThat(url.path).isEqualTo("")
        assertThat(url2.path).isEqualTo("")
        assertThat(url3.path).isEqualTo("")
    }
}