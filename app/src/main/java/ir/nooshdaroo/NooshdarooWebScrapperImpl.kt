package ir.nooshdaroo

import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.article
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h1
import it.skrape.selects.html5.h2
import it.skrape.selects.html5.h3
import it.skrape.selects.html5.img
import it.skrape.selects.html5.time
import java.net.URL

class NooshdarooWebScrapperImpl(private val nooshdarooUrl: URL) : NooshdarooWebScrapper {

    override suspend fun extractContentCategoriesWithPath(): List<Pair<String, UrlPath>> {
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
                                    text to UrlPath(url)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun extractFeaturedContents(): List<Article> {
        return skrape(AsyncFetcher) {
            request {
                url = nooshdarooUrl.toString()
                timeout = 5_000
            }
            response {
                htmlDocument {
                    div {
                        withClass = "c-featued-block__wrap"

                        val firstArticle = div {
                            withClass = "c-featued-block__main"

                            article {
                                Article(
                                    imageUrl = a {
                                        withClass = "image-link"
                                        img {
                                            findFirst {
                                                URL(attribute("src"))
                                            }
                                        }
                                    },
                                    category = div {
                                        withClass = "image-wrap"
                                        div {
                                            a {
                                                findFirst {
                                                    ownText to URL(attribute("href"))
                                                }
                                            }
                                        }
                                    },
                                    headline = div {
                                        withClass = "details"

                                        div {
                                            withClass = "headline"

                                            Headline(
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
                                    readingTime = null,
                                    articleUrl = div {
                                        withClass = "details"

                                        findFirst("a.read-more-link") {
                                            URL(attribute("href"))
                                        }
                                    }
                                )
                            }
                        }

                        val remainedArticles = div {
                            withClass = "c-featued-block__list"

                            div {
                                withClass = "c-featued-block__list-item"

                                article {
                                    findAll {
                                        map {
                                            Article(
                                                imageUrl = it.div {
                                                    withClass = "image-wrap"
                                                    div {
                                                        withClass = "image"
                                                        a {
                                                            img {
                                                                withClass = "attachment-large"
                                                                findFirst {
                                                                    runCatching {
                                                                        URL(attribute("src"))
                                                                    }.recoverCatching {
                                                                        URL(attribute("data-src"))
                                                                    }.recoverCatching {
                                                                        URL(
                                                                            attribute("data-srcset").split(" ")[0]
                                                                        )
                                                                    }.getOrThrow()
                                                                }
                                                            }
                                                        }
                                                    }
                                                },
                                                category = it.div {
                                                    withClass = "image-wrap"
                                                    div {
                                                        a {
                                                            findFirst {
                                                                ownText to URL(attribute("href"))
                                                            }
                                                        }
                                                    }
                                                },
                                                headline = it.div {
                                                    withClass = "details"

                                                    div {
                                                        withClass = "headline"

                                                        Headline(
                                                            subhead = null,
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
                                                        URL(attribute("href"))
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        listOf(firstArticle) + remainedArticles
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
                                            URL(attribute("data-video-url")) to URL(attribute("data-video-poster"))
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
                                                        URL(attribute("src"))
                                                    }.recoverCatching {
                                                        URL(attribute("data-src"))
                                                    }.recoverCatching {
                                                        URL(
                                                            attribute("data-srcset").split(" ")[0]
                                                        )
                                                    }.getOrThrow()
                                                }
                                            }
                                        }
                                    }

                                    val (headline, articleUrl) = findSecond {
                                        div {
                                            val (title, articleUrl) = h3 {
                                                a {
                                                    findFirst { ownText to URL(attribute("href")) }
                                                }
                                            }
                                            val subhead = div {
                                                withClass = "subhead"
                                                findFirst { ownText }
                                            }

                                            Pair(Headline(subhead, title), articleUrl)
                                        }
                                    }

                                    Article(
                                        category = null,
                                        imageUrl = imageUrl,
                                        articleUrl = articleUrl,
                                        headline = headline,
                                        readingTime = null
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
