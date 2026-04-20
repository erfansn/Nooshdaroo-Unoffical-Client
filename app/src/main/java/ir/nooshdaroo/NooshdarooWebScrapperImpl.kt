package ir.nooshdaroo

import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.and
import it.skrape.selects.attribute
import it.skrape.selects.html5.a
import it.skrape.selects.html5.article
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h1
import it.skrape.selects.html5.h2
import it.skrape.selects.html5.h3
import it.skrape.selects.html5.img
import it.skrape.selects.html5.p
import it.skrape.selects.html5.section
import it.skrape.selects.html5.style
import it.skrape.selects.html5.time
import java.net.URL

class NooshdarooWebScrapperImpl(private val nooshdarooUrl: URL) : NooshdarooWebScrapper {

    override suspend fun extractContentCategoriesWithPath(): List<Category> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    div {
                        withClass = "c-header-side__wrap"
                        div {
                            withClass = "c-nav-block__side-menu"
                            findFirst {
                                eachLink.entries.map { (text, url) ->
                                    Category(text, Url(nooshdarooUrl.toString().dropLast(1) + url))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractFeaturedContents(): List<Content> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    div {
                        withClass = "c-featued-block__wrap"

                        val firstContent = div {
                            withClass = "c-featued-block__main"

                            article {
                                Content(
                                    category = div {
                                        withClass = "image-wrap"
                                        div {
                                            a {
                                                findFirst {
                                                    Category(ownText, Url(attribute("href")))
                                                }
                                            }
                                        }
                                    },
                                    article = Article(
                                        imageUrl = a {
                                            withClass = "image-link"
                                            img {
                                                findFirst {
                                                    Url(attribute("src"))
                                                }
                                            }
                                        },
                                        description = div {
                                            withClass = "details"

                                            div {
                                                withClass = "headline"

                                                Description(
                                                    subhead = div {
                                                        withClass = "subhead"

                                                        findFirst {
                                                            ownText
                                                        }
                                                    },
                                                    title = h1 {
                                                        a {
                                                            findFirst {
                                                                ownText
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        },
                                        articleUrl = div {
                                            withClass = "details"

                                            findFirst("a.read-more-link") {
                                                Url(attribute("href"))
                                            }
                                        }
                                    )
                                )
                            }
                        }

                        val remainedContents = div {
                            withClass = "c-featued-block__list"

                            div {
                                withClass = "c-featued-block__list-item"

                                article {
                                    findAll {
                                        map {
                                            Content(
                                                category = it.div {
                                                    withClass = "image-wrap"
                                                    div {
                                                        a {
                                                            findFirst {
                                                                Category(ownText, Url(attribute("href")))
                                                            }
                                                        }
                                                    }
                                                },
                                                article = Article(
                                                    imageUrl = it.div {
                                                        withClass = "image-wrap"
                                                        div {
                                                            withClass = "image"
                                                            a {
                                                                img {
                                                                    withClass = "attachment-large"
                                                                    findFirst {
                                                                        runCatching {
                                                                            Url(attribute("src"))
                                                                        }.recoverCatching {
                                                                            Url(attribute("data-src"))
                                                                        }.recoverCatching {
                                                                            Url(
                                                                                attribute("data-srcset").split(" ")[0]
                                                                            )
                                                                        }.getOrThrow()
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    },
                                                    description = it.div {
                                                        withClass = "details"

                                                        div {
                                                            withClass = "headline"

                                                            Description(
                                                                title = h2 {
                                                                    a {
                                                                        findFirst {
                                                                            ownText
                                                                        }
                                                                    }
                                                                }
                                                            )
                                                        }
                                                    },
                                                    readingTime = it.div {
                                                        withClass = "details"

                                                        time {
                                                            findFirst {
                                                                ownText
                                                            }
                                                        }
                                                    },
                                                    articleUrl = it.div {
                                                        withClass = "details"

                                                        findFirst("div.headline h2 a") {
                                                            Url(attribute("href"))
                                                        }
                                                    }
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        listOf(firstContent) + remainedContents
                    }
                }
            }
        }
    }

    override suspend fun extractShortVideos(): List<ShortVideo> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    div {
                        withClass = "c-short-videos-block__list-item"

                        findAll {
                            map {
                                it.article {
                                    a {
                                        val (videoUrl, posterUrl) = findFirst {
                                            Url(attribute("data-video-url")) to Url(attribute("data-video-poster"))
                                        }

                                        val duration = div {
                                            withClass = "image-wrap"

                                            div {
                                                findAll {
                                                    firstOrNull { it.hasClass("tax-label") }?.ownText
                                                }
                                            }
                                        }

                                        ShortVideo(
                                            duration = duration,
                                            videoUrl = videoUrl,
                                            posterUrl = posterUrl
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractLatestContentInDigitalLiteracy(): List<Article> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    div {
                        withClass = "c-digital-literacy__slider-item"
                        findAll {
                            map {
                                it.div {
                                    val imageUrl = findFirst {
                                        a {
                                            img {
                                                findFirst {
                                                    runCatching {
                                                        Url(attribute("src"))
                                                    }.recoverCatching {
                                                        Url(attribute("data-src"))
                                                    }.recoverCatching {
                                                        Url(
                                                            attribute("data-srcset").split(" ")[0]
                                                        )
                                                    }.getOrThrow()
                                                }
                                            }
                                        }
                                    }

                                    val (description, articleUrl) = findSecond {
                                        div {
                                            val (title, articleUrl) = h3 {
                                                a {
                                                    findFirst { ownText to Url(attribute("href")) }
                                                }
                                            }
                                            val subhead = div {
                                                withClass = "subhead"
                                                findFirst { ownText }
                                            }

                                            Pair(Description(subhead, title), articleUrl)
                                        }
                                    }

                                    Article(
                                        imageUrl = imageUrl,
                                        articleUrl = articleUrl,
                                        description = description,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractLatestVideos(): List<Video> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    div {
                        withClass = "c-videos-block__list-item"
                        article {
                            findAll {
                                map {
                                    val (duration, posterUrl) = it.div {
                                        withClass = "image-wrap"

                                        val duration = div {
                                            findAll {
                                                find { it.hasClass("tax-label") }?.ownText
                                            }
                                        }
                                        val posterUrl = findFirst("div.image a img") {
                                            runCatching {
                                                Url(attribute("src"))
                                            }.recoverCatching {
                                                Url(attribute("data-src"))
                                            }.recoverCatching {
                                                Url(
                                                    attribute("data-srcset").split(" ")[0]
                                                )
                                            }.getOrThrow()
                                        }
                                        duration to posterUrl
                                    }
                                    val (videoUrl, description) = it.div {
                                        withClass = "details"

                                        val (videoUrl, title) = div {
                                            withClass = "headline"
                                            h3 {
                                                a {
                                                    findFirst {
                                                        Url(attribute("href")) to ownText
                                                    }
                                                }
                                            }
                                        }
                                        val subhead = div {
                                            withClass = "excerpt"
                                            p {
                                                findFirst { ownText }
                                            }
                                        }

                                        videoUrl to Description(title, subhead)
                                    }

                                    Video(
                                        description = description,
                                        videoUrl = videoUrl,
                                        posterUrl = posterUrl,
                                        duration = duration
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractLatestContent(): List<Content> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    section {
                        withClass = "c-articles-block"
                        findAll {
                            first {
                                !it.hasClass("is-style-alt")
                            }.selection("div div.c-articles-block__wrap div") {
                                div {
                                    withClass = "c-articles-block__list-item"

                                    article {
                                        findAll {
                                            map {
                                                val (category, imageUrl) = it.div {
                                                    withClass = "image-wrap"

                                                    val category = findFirst("div.tax-label") {
                                                        Category(title = ownText)
                                                    }
                                                    val imageUrl = findFirst("div.image a img") {
                                                        runCatching {
                                                            Url(attribute("src"))
                                                        }.recoverCatching {
                                                            Url(attribute("data-src"))
                                                        }.recoverCatching {
                                                            Url(
                                                                attribute("data-srcset").split(" ")[0]
                                                            )
                                                        }.getOrThrow()
                                                    }
                                                    category to imageUrl
                                                }

                                                val (articleUrl, description, readingTime) = it.div {
                                                    withClass = "details"

                                                    val (articleUrl, title) = div {
                                                        withClass = "headline"
                                                        h3 {
                                                            a {
                                                                findFirst {
                                                                    Url(attribute("href")) to ownText
                                                                }
                                                            }
                                                        }
                                                    }
                                                    val readingTime = div {
                                                        withClass = "reading-time"
                                                        findFirst { ownText }
                                                    }

                                                    Triple(articleUrl, Description(title), readingTime)
                                                }

                                                Content(
                                                    category = category,
                                                    article = Article(
                                                        imageUrl = imageUrl,
                                                        description = description,
                                                        readingTime = readingTime,
                                                        articleUrl = articleUrl
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractDailyNote(): String {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    div {
                        withClass = "today-note"

                        p {
                            findFirst {
                                ownText
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractLatestInParentalControl(): List<Article> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    section {
                        withClass = "c-articles-alt-block"
                        findAll {
                            first {
                                !it.hasClass("is-style-alt")
                            }.selection("div div.c-articles-alt-block__wrap div") {
                                div {
                                    withClass = "c-articles-alt-block__list-item"
                                    article {
                                        findAll {
                                            map {
                                                val imageUrl = it.div {
                                                    withClass = "image-wrap"

                                                    findFirst("div a img") {
                                                        runCatching {
                                                            Url(attribute("src"))
                                                        }.recoverCatching {
                                                            Url(attribute("data-src"))
                                                        }.recoverCatching {
                                                            Url(
                                                                attribute("data-srcset").split(" ")[0]
                                                            )
                                                        }.getOrThrow()
                                                    }
                                                }

                                                val (articleUrl, description, readingTime) = it.div {
                                                    withClass = "details"

                                                    val (articleUrl, title) = div {
                                                        withClass = "headline"
                                                        h3 {
                                                            a {
                                                                findFirst {
                                                                    Url(attribute("href")) to ownText
                                                                }
                                                            }
                                                        }
                                                    }
                                                    val readingTime = div {
                                                        withClass = "reading-time"
                                                        findFirst { ownText }
                                                    }
                                                    Triple(
                                                        articleUrl,
                                                        Description(title),
                                                        readingTime
                                                    )
                                                }

                                                Article(
                                                    imageUrl = imageUrl,
                                                    description = description,
                                                    readingTime = readingTime,
                                                    articleUrl = articleUrl
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractPopularContents(): List<Content> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    section {
                        withClass = "c-articles-block"
                        findAll {
                            first {
                                it.hasClass("is-style-alt")
                            }.selection("div div.c-articles-block__wrap div") {
                                div {
                                    withClass = "c-articles-block__list-item"

                                    article {
                                        findAll {
                                            map {
                                                val (category, imageUrl) = it.div {
                                                    withClass = "image-wrap"

                                                    val category = findFirst("div.tax-label") {
                                                        Category(title = ownText)
                                                    }
                                                    val imageUrl = findFirst("div.image a img") {
                                                        runCatching {
                                                            Url(attribute("src"))
                                                        }.recoverCatching {
                                                            Url(attribute("data-src"))
                                                        }.recoverCatching {
                                                            Url(
                                                                attribute("data-srcset").split(" ")[0]
                                                            )
                                                        }.getOrThrow()
                                                    }
                                                    category to imageUrl
                                                }

                                                val (articleUrl, description, readingTime) = it.div {
                                                    withClass = "details"

                                                    val (articleUrl, title) = div {
                                                        withClass = "headline"
                                                        h3 {
                                                            a {
                                                                findFirst {
                                                                    Url(attribute("href")) to ownText
                                                                }
                                                            }
                                                        }
                                                    }
                                                    val readingTime = div {
                                                        withClass = "reading-time"
                                                        findFirst { ownText }
                                                    }

                                                    Triple(articleUrl, Description(title), readingTime)
                                                }

                                                Content(
                                                    category = category,
                                                    article = Article(
                                                        imageUrl = imageUrl,
                                                        description = description,
                                                        readingTime = readingTime,
                                                        articleUrl = articleUrl
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractShouldKnowInEmergencyStateArticles(): List<Article> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    section {
                        withClass = "c-articles-alt-block"
                        findAll {
                            first {
                                it.hasClass("is-style-alt")
                            }.selection("div div.c-articles-alt-block__wrap div") {
                                div {
                                    withClass = "c-articles-alt-block__list-item"
                                    article {
                                        a {
                                            findAll {
                                                map {
                                                    val imageUrl = it.div {
                                                        withClass = "image-wrap"
                                                        findFirst("div img") {
                                                            runCatching {
                                                                Url(attribute("src"))
                                                            }.recoverCatching {
                                                                Url(attribute("data-src"))
                                                            }.recoverCatching {
                                                                Url(
                                                                    attribute("data-srcset").split(" ")[0]
                                                                )
                                                            }.getOrThrow()
                                                        }
                                                    }

                                                    val description = it.div {
                                                        withClass = "details"

                                                        val title = div {
                                                            h3 {
                                                                findFirst {
                                                                    ownText
                                                                }
                                                            }
                                                        }
                                                        Description(title)
                                                    }

                                                    val articleUrl = Url(it.attribute("href"))

                                                    Article(
                                                        imageUrl = imageUrl,
                                                        description = description,
                                                        articleUrl = articleUrl
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
