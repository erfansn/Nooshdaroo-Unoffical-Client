package ir.nooshdaroo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UrlTest {

    @Test(expected = IllegalArgumentException::class)
    fun throw_exception_when_passed_address_is_invalid() {
        Url("http://localhost")
    }

    @Test
    fun returns_path_correctly_when_url_with_long_path_passed() {
        val url = Url("http://host.com:3232/a/b/c?param=value#fragment")

        assertThat(url.path).isEqualTo("/a/b/c")
    }

    @Test
    fun returns_empty_as_path_when_no_path_in_passed_url() {
        val url = Url("http://host.com/")
        val url2 = Url("http://host.com")
        val url3 = Url("http://host.com?param=value")
        val url4 = Url("http://host.com?param=value#anchor")

        assertThat(url.path).isNull()
        assertThat(url2.path).isNull()
        assertThat(url3.path).isNull()
        assertThat(url4.path).isNull()
    }
}