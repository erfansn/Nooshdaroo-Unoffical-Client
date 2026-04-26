package ir.nooshdaroo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import ir.nooshdaroo.data.NooshdarooWebScrapper
import ir.nooshdaroo.data.NooshdarooWebScrapperImpl
import ir.nooshdaroo.data.model.Article
import ir.nooshdaroo.data.model.Category
import ir.nooshdaroo.data.model.Content
import ir.nooshdaroo.data.model.Description
import ir.nooshdaroo.data.model.ShortVideo
import ir.nooshdaroo.data.model.Video
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NooshdarooWebScrapperImplTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private val mainPage = javaClass.getResourceAsStream("/index.html")!!.reader().use { it.readText() }

    private lateinit var nooshdarooWebScrapper: NooshdarooWebScrapper

    @Before
    fun setUp() {
        mockWebServer.enqueue(MockResponse().setBody(mainPage).setResponseCode(200))

        nooshdarooWebScrapper = NooshdarooWebScrapperImpl(mockWebServer.url("/").toUrl())
    }

    @Test
    fun mainPage_extractingCategories_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractContentCategoriesWithPath()

        assertThat(result).apply {
            hasSize(15)

            comparingElementsUsing(CategoryCorrespondence).containsExactly(
                Category(
                    "همه‌ی مطالب",
                    Url("http://localhost:63210/all")
                ),
                Category(
                    "بحران و شرایط اضطراری",
                    Url("http://localhost:63210/crisis-and-war/")
                ),
                Category(
                    "خبر و تحلیل",
                    Url("http://localhost:63210/news-opinion/")
                ),
                Category(
                    "مبانی امنیت",
                    Url("http://localhost:63210/cybersecurity-basics/")
                ),
                Category(
                    "حریم خصوصی",
                    Url("http://localhost:63210/privacy-protection/")
                ),
                Category(
                    "انواع کلاهبرداری",
                    Url("http://localhost:63210/fraud-awareness/")
                ),
                Category(
                    "امنیت در تلفن هوشمند",
                    Url("http://localhost:63210/smartphone-security/")
                ),
                Category(
                    "خرید آنلاین",
                    Url("http://localhost:63210/secure-online-shopping/")
                ),
                Category(
                    "فروش آنلاین",
                    Url("http://localhost:63210/safe-ecommerce-selling/")
                ),
                Category(
                    "معاملات دست دوم",
                    Url("http://localhost:63210/safe-secondhand-deals/")
                ),
                Category(
                    "ابزارها و افزونه‌ها",
                    Url("http://localhost:63210/security-tools/")
                ),
                Category(
                    "راهنمای والدین",
                    Url("http://localhost:63210/parental-control/")
                ),
                Category(
                    "ارزهای دیجیتال",
                    Url("http://localhost:63210/crypto-security/")
                ),
                Category(
                    "راهنماهای قانونی",
                    Url("http://localhost:63210/legal-guides/")
                ),
                Category(
                    "سواد دیجیتال",
                    Url("http://localhost:63210/digital-literacy/")
                )
            )
        }
    }

    @Test
    fun mainPage_extractingFeaturedContents_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractFeaturedContents()

        assertThat(result).apply {
            hasSize(13)

            containsExactly(
                Content(
                    Category(
                        title = "خبر و تحلیل",
                        url = Url("https://nooshdaroo.ir/news-opinion/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da04ce3c3ec.webp"),
                        description = Description(
                            title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی‌ها آغاز شد",
                            subhead = "مراقب دارایی خود باشید!"
                        ),
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/dai-to-usds-auto-conversion-warning/")
                    )
                ),
                Content(
                    Category(
                        title = "خبر و تحلیل",
                        url = Url("https://nooshdaroo.ir/news-opinion/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da12866fd3b-1024x576.webp"),
                        description = Description(
                            title = "کره جنوبی دسترسی به «اینترنت پایه» را برای شهروندان رایگان می‌کند"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/south-korea-universal-internet-access/")
                    )
                ),
                Content(
                    Category(
                        title = "راهنماهای قانونی",
                        url = Url("https://nooshdaroo.ir/legal-guides/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d96ff009fad-1024x576.webp"),
                        description = Description(
                            title = "آشنایی با «هوش مصنوعی سایه»: وقتی کارمندان قوانین امنیتی را دور می‌زنند!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/legal-guides/shadow-ai-security-risks/")
                    )
                ),
                Content(
                    Category(
                        title = "امنیت در تلفن هوشمند",
                        url = Url("https://nooshdaroo.ir/smartphone-security/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d92187b1081-1024x576.webp"),
                        description = Description(
                            title = "چگونه FBI پیام‌های حذف‌شده‌ی پیام‌رسان سیگنال را از آیفون استخراج کرد؟"
                        ),
                        readingTime = "زمان مطالعه ۳ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/smartphone-security/fbi-accessed-signal-messages-via-ios/")
                    )
                ),
                Content(
                    Category(
                        title = "خبر و تحلیل",
                        url = Url("https://nooshdaroo.ir/news-opinion/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d8c73a852e8-1024x576.webp"),
                        description = Description(
                            title = "دروغ بزرگ واتساپ؛ شاید پیام‌های شما هرگز خصوصی نبوده‌اند!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/whatsapp-under-fire-for-privacy-practices/")
                    )
                ),
                Content(
                    Category(
                        title = "مبانی امنیت",
                        url = Url("https://nooshdaroo.ir/cybersecurity-basics/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d75b1d512fc-1024x576.webp"),
                        description = Description(
                            title = "آیا بیت‌لاکر واقعاً امن است؟ وقتی کلیدهای رمزنگاری به دست FBI می‌رسد"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/cybersecurity-basics/is-bitlocker-safe/")
                    )
                ),
                Content(
                    Category(
                        title = "حریم خصوصی",
                        url = Url("https://nooshdaroo.ir/privacy-protection/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d0dc3dbe019-1024x576.webp"),
                        description = Description(
                            title = "خطرات داده بیومتریک در عصر دیجیتال: بدن شما چگونه حریم خصوصی‌تان را لو می‌دهد؟"
                        ),
                        readingTime = "زمان مطالعه ۷ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/privacy-protection/biometric-data-risks/")
                    )
                ),
                Content(
                    Category(
                        title = "خبر و تحلیل",
                        url = Url("https://nooshdaroo.ir/news-opinion/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d68635763e3-1024x576.webp"),
                        description = Description(
                            title = "هوش مصنوعی جدید آنتروپیک هزاران آسیب‌پذیری در معتبرترین سیستم‌ها پیدا کرد!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/anthropic-claude-mythos-glasswing/")
                    )
                ),
                Content(
                    Category(
                        title = "سواد دیجیتال",
                        url = Url("https://nooshdaroo.ir/digital-literacy/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d3fac56689d-1024x576.webp"),
                        description = Description(
                            title = "چرا گوگل، متا و مایکروسافت در حال ساخت نیروگاه‌های گازی هستند؟"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/digital-literacy/ai-data-centers-natural-gas/")
                    )
                ),
                Content(
                    Category(
                        title = "خبر و تحلیل",
                        url = Url("https://nooshdaroo.ir/news-opinion/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d350ee31650-1024x576.webp"),
                        description = Description(
                            title = "وقتی هوش مصنوعی برای محافظت از هم‌نوع خود، در برابر انسان‌ می‌ایستد!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/ai-defying-human-commands/")
                    )
                ),
                Content(
                    Category(
                        title = "بحران و شرایط اضطراری",
                        url = Url("https://nooshdaroo.ir/crisis-and-war/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d55074dba6e-1024x576.webp"),
                        description = Description(
                            title = "خطوط جایگزین برای تماس با اورژانس «تهران» در شرایط اضطراری اعلام شد"
                        ),
                        readingTime = "زمان مطالعه ۱ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/tehran-emergency-alternative-lines/")
                    )
                ),
                Content(
                    Category(
                        title = "خبر و تحلیل",
                        url = Url("https://nooshdaroo.ir/news-opinion/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d258577b258-1024x576.webp"),
                        description = Description(
                            title = "سایه سنگین قطعی اینترنت بر بازار کریپتو: سقوط ۸۰ درصدی معاملات در ایران"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/iran-crypto-ecosystem-collapsing/")
                    )
                ),
                Content(
                    Category(
                        title = "حریم خصوصی",
                        url = Url("https://nooshdaroo.ir/privacy-protection/")
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d117890f221-1024x576.webp"),
                        description = Description(
                            title = "رسوایی بزرگ لینکدین و مایکروسافت: آیا کامپیوتر کاربران بدون اجازه تفتیش می‌شود؟!"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/privacy-protection/linkedin-fairlinked-leak/")
                    )
                )
            )
        }
    }

    @Test
    fun mainPage_extractingShortVideos_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractShortVideos()

        assertThat(result).apply {
            hasSize(11)

            containsAnyOf(
                ShortVideo(
                    duration = "۰۰:۵۸",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d74ae42a21e.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/294e3c21-058b-4294-aa16-24aa35a9aa7e/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۱:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69b96177d4ed9.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/261616f5-2df0-4ec8-b231-cfa07f541055/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۱:۵۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69afc29ccdae1.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/36b87710-3b4d-4c41-a8eb-8f0c91d41a2e/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۱:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_69a2ad9beb197.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/e2cfd799-3bba-49c0-818b-f9b93d13c8f1/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۲:۴۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b6176b052.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/d28cfa0e-18b4-4826-ad80-f2b94d96a1cf/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۱:۰۱",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69a53e982c869.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/a9cfe7b3-e4b9-43da-bfa0-e718ee230e7b/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۲:۲۳",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b0d48e5a2.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/f43502c0-7de2-4dcb-9b5d-ba6cbce41918/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۱:۲۷",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_698b22df629c3.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/0a31d59b-7c59-40b9-8138-b02fc741a3a5/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۱:۱۲",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6972370b71e18-576x1024.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/483f7bdf-c8d8-4355-b695-e12e8333b0ec/master.m3u8")
                ),
                ShortVideo(
                    duration = "۰۰:۵۷",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_69690bea5b429.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/da8af596-cd27-456e-9d58-2f772964cbe5/master.m3u8")
                ),
                ShortVideo(
                    duration = "null",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_68550a04e843f.webp"),
                    videoUrl = Url("https://ganjoor.nooshdaroo.ir/videos/75022b8d-e3dc-4700-ab86-54332e9ed501/master.m3u8")
                )
            )
        }
    }

    @Test
    fun mainPage_extractingLatestDigitalLiteracyContent_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractLatestContentInDigitalLiteracy()

        assertThat(result).apply {
            hasSize(9)

            containsExactly(
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d96ff009fad-768x432.webp"),
                    description = Description(
                        title = "میان‌برهای پرهزینه …",
                        subhead = "آشنایی با «هوش مصنوعی سایه»: وقتی کارمندان قوانین امنیتی را دور می‌زنند!"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/legal-guides/shadow-ai-security-risks/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d3fac56689d-768x432.webp"),
                    description = Description(
                        title = "بحران انرژی در راه است؟",
                        subhead = "چرا گوگل، متا و مایکروسافت در حال ساخت نیروگاه‌های گازی هستند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/ai-data-centers-natural-gas/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d350ee31650-768x432.webp"),
                    description = Description(
                        title = "روی تاریک و پیش‌بینی‌نشده‌ی هوش مصنوعی",
                        subhead = "وقتی هوش مصنوعی برای محافظت از هم‌نوع خود، در برابر انسان‌ می‌ایستد!"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/news-opinion/ai-defying-human-commands/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d0dc98b0cbe-768x432.webp"),
                    description = Description(
                        title = "انقلاب جدید گوگل در روزهای خاموشی اینترنت ایران!",
                        subhead = "گوگل Gemma 4 را معرفی کرد؛ مدل‌های متن‌باز رایگان با امکان اجرای آفلاین"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/news-opinion/google-gemma4/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c9102153f3d-768x432.webp"),
                    description = Description(
                        title = "«توجه! توجه! توجه!»",
                        subhead = "درباره ایستگاه رادیویی مرموزی که همزمان با جنگ شروع به پخش کرد چه می‌دانیم؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/numbers-station-during-iran-usa-war/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c46c12443ce-768x432.webp"),
                    description = Description(
                        title = "کرک‌هایی خطرناک‌تر از همیشه",
                        subhead = "توصیه به گیمرها: به‌هیچ‌وجه سراغ نسل جدید بازی‌های کرکی (Hypervisor) نروید"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/parental-control/hypervisor-cracks-dangers/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c8fd8b65b-768x432.avif"),
                    description = Description(
                        title = "شبکه‌ای که می‌بیند و حس می‌کند …",
                        subhead = "آشنایی با شبکه 6G: نسل بعدی اینترنت موبایل"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/6g-explained/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c0655dfc2-768x432.webp"),
                    description = Description(
                        title = "مشکلِ قابل ‌پیش‌بینی بودن!",
                        subhead = "چرا پسوردهای ساده خیلی راحت فاش می‌شوند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/why-simple-passwords-get-hacked-easily/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3a24690722-768x432.webp"),
                    description = Description(
                        title = "اتفاقی آنقدر عادی که دیگر برایمان عجیب نیست!",
                        subhead = "معجزه در کف اقیانوس: اطلاعاتِ اینترنتی چطور در کسری از ثانیه جابه‌جا می‌شوند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/how-submarine-cables-work/")
                )
            )
        }
    }

    @Test
    fun mainPage_extractingLatestVideos_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractLatestVideos()

        assertThat(result).apply {
            hasSize(5)

            containsExactly(
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/crisis-and-war/using-tourniquet-to-stop-bleeding/"),
                    description = Description(
                        title = "نحوه کنترل خونریزی شدید دست و پا در محل حادثه (کمک‌های اولیه)",
                        subhead = ""
                    ),
                    duration = "۰۳:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_6857c77c2a9b5.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/crisis-and-war/first-aid-in-first-minitue/"),
                    description = Description(
                        title = "کمک‌های اولیه در نخستین لحظات حادثه",
                        subhead = ""
                    ),
                    duration = "۰۳:۳۴",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_685651e38fea6.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/smartphone-security/7-essential-telegram-security/"),
                    description = Description(
                        title = "هفت تنظیم ضروری تلگرام برای امنیت و حفاظت از حریم شخصی",
                        subhead = "تلگرام به همان اندازه که می‌تواند ابزار امنی برای مکالمه با دیگران باشد، می‌تواند ابزاری باشد برای لو دادن اطلاعات خصوصی و فعالیت‌هایمان! بنابراین ضروری"
                    ),
                    duration = "۰۵:۳۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6988724958faf.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/fraud-awareness/sex-scam-shame-trap/"),
                    description = Description(
                        title = "کلاه‌برداری صیغه و رابطه جنسی: وقتی کلاه‌بردار روی حس شرم دست می‌گذارد!",
                        subhead = "با اندکی جست‌وجو در تلگرام و اینستاگرام، می‌توان با انبوهی از کانال‌ها و صفحات روبه‌رو شد که وعده «صیغه» و «ماساژ» می‌دهند، اما در واقع"
                    ),
                    duration = "۰۲:۰۳",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/10/nooshdaroo_68f338f6beb85.webp")
                ),
                Video(
                    videoUrl = Url("https://nooshdaroo.ir/videos/legal-guides/report-scam-and-get-refund/"),
                    description = Description(
                        title = "از گزارش تخلف تا بازگرداندن پول در پلتفرم‌های ثبت آگهی: چگونه از کلاه‌برداران شکایت کنیم؟",
                        subhead = "هنگامی‌ که در یک پلتفرم ثبت آگهی مورد کلاهبرداری قرار می‌گیریم، لازم است مجموعه‌ای از مراحل مشخص را طی کنیم تا شانس واقعی برای بازگرداندن"
                    ),
                    duration = "۰۳:۰۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/10/nooshdaroo_68e624431bf5a.webp")
                )
            )
        }
    }

    @Test
    fun mainPage_extractingLatestContent_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractLatestContent()

        assertThat(result).apply {
            hasSize(3)

            containsExactly(
                Content(
                    Category(
                        title = "خبر و تحلیل",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da04ce3c3ec.webp"),
                        description = Description(
                            title = "زنگ خطر برای کاربران ایرانی: تبدیل خودکار توکن DAI به USDS در صرافی‌ها آغاز شد"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/dai-to-usds-auto-conversion-warning/")
                    )
                ),
                Content(
                    Category(
                        title = "خبر و تحلیل",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69da12866fd3b.webp"),
                        description = Description(
                            title = "کره جنوبی دسترسی به «اینترنت پایه» را برای شهروندان رایگان می‌کند"
                        ),
                        readingTime = "زمان مطالعه ۲ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/south-korea-universal-internet-access/")
                    )
                ),
                Content(
                    Category(
                        title = "خبر و تحلیل",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d92bf5b6773.webp"),
                        description = Description(
                            title = "زنگ خطر برای کاربران ادوبی ریدر؛ کشف آسیب‌پذیری خطرناک در فایل‌های PDF ناشناس"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/news-opinion/adobe-reader-pdf-zero-day-vulnerability/")
                    )
                )
            )
        }
    }

    @Test
    fun mainPage_extractingDailyNote_returnsCorrectly() = runTest {
        val todayNote = nooshdarooWebScrapper.extractDailyNote()

        assertThat(todayNote).isEqualTo("اگر در پیامکی خواندی که حساب بانکی\u200Cات مسدود شده، نخست ببین که اصلاً در آن بانک حساب داری یا نه!")
    }

    @Test
    fun mainPage_extractingLatestInParentalControl_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractLatestInParentalControl()

        assertThat(result).apply {
            hasSize(4)

            containsExactly(
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c46c12443ce-300x169.webp"),
                    description = Description(
                        title = "توصیه به گیمرها: به‌هیچ‌وجه سراغ نسل جدید بازی‌های کرکی (Hypervisor) نروید"
                    ),
                    readingTime = "زمان مطالعه ۶ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/parental-control/hypervisor-cracks-dangers/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_69957fa3ad211-300x169.webp"),
                    description = Description(
                        title = "راهکارهای عملی: چطور در شرایط بحران و جنگ، آرامش فرزندمان را حفظ کنیم؟"
                    ),
                    readingTime = "زمان مطالعه ۷ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/child-peace-in-crisis/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_698395cf05cd9-300x169.webp"),
                    description = Description(
                        title = "چرا کودکان امروزی مضطرب‌ترند؟ ردپای تکنولوژی در سلامت روان"
                    ),
                    readingTime = "زمان مطالعه ۸ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/parental-control/childhood-stress/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6967e3e48634f-300x169.webp"),
                    description = Description(
                        title = "راه‌های کاهش استرس کودک در شرایط بحرانی؛ توصیه‌های مهم برای والدین"
                    ),
                    readingTime = "زمان مطالعه ۷ دقیقه",
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/reducing-child-stress-in-crisis/")
                )
            )
        }
    }

    @Test
    fun mainPage_extractingPopularContents_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractPopularContents()

        assertThat(result).apply {
            hasSize(5)

            containsExactly(
                Content(
                    Category(
                        title = "بحران و شرایط اضطراری",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6964e0c11c4ef.webp"),
                        description = Description(
                            title = "لینک دسترسی به برخی سایت‌‌ها برای دوران قطعی اینترنت"
                        ),
                        readingTime = "زمان مطالعه ۵ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/access-list/")
                    )
                ),
                Content(
                    Category(
                        title = "بحران و شرایط اضطراری",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_699aeed4d2032.webp"),
                        description = Description(
                            title = "راهنمای نگهداری گوشت در بحران و بی‌برقی"
                        ),
                        readingTime = "زمان مطالعه ۱۰ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/meat-preservation-in-crisis/")
                    )
                ),
                Content(
                    Category(
                        title = "بحران و شرایط اضطراری",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_685101130d31c.webp"),
                        description = Description(
                            title = "۱۰ نکته برای مصرف بنزین کمتر هنگام رانندگی"
                        ),
                        readingTime = "زمان مطالعه ۳ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/10-fuel-efficient-driving-techniques/")
                    )
                ),
                Content(
                    Category(
                        title = "بحران و شرایط اضطراری",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69b9bdabed901.webp"),
                        description = Description(
                            title = "بعد از شکستن شیشه‌ها بر اثر موج انفجار چه کنیم؟"
                        ),
                        readingTime = "زمان مطالعه ۴ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/what-to-do-after-blast/")
                    )
                ),
                Content(
                    Category(
                        title = "بحران و شرایط اضطراری",
                    ),
                    Article(
                        imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69aeeac7477d5.webp"),
                        description = Description(
                            title = "کاهش خطر پرتاب شیشه در لحظه انفجار: چه کنیم و چه نکنیم؟"
                        ),
                        readingTime = "زمان مطالعه ۵ دقیقه",
                        articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/window-safety-during-explosion/")
                    )
                )
            )
        }
    }

    @Test
    fun mainPage_extractingShouldKnowInEmergencyStateArticles_returnsCorrectly() = runTest {
        val result = nooshdarooWebScrapper.extractShouldKnowInEmergencyStateArticles()

        assertThat(result).apply {
            hasSize(4)

            containsExactly(
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d55074dba6e-300x169.webp"),
                    description = Description(
                        title = "خطوط جایگزین برای تماس با اورژانس «تهران» در شرایط اضطراری اعلام شد"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/tehran-emergency-alternative-lines/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d4d98915b14-300x169.webp"),
                    description = Description(
                        title = "چطور لباس‌ها را با کم‌ترین مقدار آب بشوییم و بهداشت فردی را حفظ کنیم؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/washing-clothes-with-less-water/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/08/nooshdaroo_68a310cb05bdc-300x169.webp"),
                    description = Description(
                        title = "هفت نشانه‌ی کلاهبرداری ارز دیجیتال در زمان قطعی اینترنت"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crypto-security/crypto-scam-signs-in-crisis/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69ba9e321f576-300x169.webp"),
                    description = Description(
                        title = "۶ راهکار برای بازگردانی امنیت روانی به محیط خانه"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/crisis-and-war/mental-peace-at-home/")
                )
            )
        }
    }
}
