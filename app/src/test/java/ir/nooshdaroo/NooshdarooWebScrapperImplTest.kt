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

        nooshdarooWebScrapper = NooshdarooWebScrapperImpl(mockWebServer.url("").toUrl())
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
                    "خبر و تحلیل",
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
                    "خبر و تحلیل",
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
                    "راهنماهای قانونی",
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
                    "امنیت در تلفن هوشمند",
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
                    "خبر و تحلیل",
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
                    "مبانی امنیت",
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
                    "حریم خصوصی",
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
                    "خبر و تحلیل",
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
                    "سواد دیجیتال",
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
                    "خبر و تحلیل",
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
                    "بحران و شرایط اضطراری",
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
                    "خبر و تحلیل",
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
                    "حریم خصوصی",
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
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/scam-alert-non-existent-sim-cards/")
                ),
                ShortVideo(
                    duration = "۰۱:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69b96177d4ed9.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crypto-security/crypto-frauds-while-disconnecting-internet/")
                ),
                ShortVideo(
                    duration = "۰۱:۵۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69afc29ccdae1.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/protection-digital-currency-war/")
                ),
                ShortVideo(
                    duration = "۰۱:۳۶",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_69a2ad9beb197.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/protect-identity-in-crisis/")
                ),
                ShortVideo(
                    duration = "۰۲:۴۵",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b6176b052.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/power-water-outage-essential-needs/")
                ),
                ShortVideo(
                    duration = "۰۱:۰۱",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69a53e982c869.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/crisis-and-war/%d8%aa%d9%86%d8%b8%db%8c%d9%85%d8%a7%d8%aa-%d9%85%d9%87%d9%85-%d8%b3%db%8c%d8%b1%db%8c-%da%a9%d9%87-%d8%b4%d8%a7%db%8c%d8%af-%db%8c%da%a9-%d8%b1%d9%88%d8%b2-%d8%ac%d8%a7%d9%86%d8%aa%d8%a7%d9%86-%d8%b1/")
                ),
                ShortVideo(
                    duration = "۰۲:۲۳",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_6996b0d48e5a2.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/fix-site-loading-digital-privacy/")
                ),
                ShortVideo(
                    duration = "۰۱:۲۷",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/02/nooshdaroo_698b22df629c3.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/dns-security-guide/")
                ),
                ShortVideo(
                    duration = "۰۱:۱۲",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_6972370b71e18-576x1024.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/security-tools/what-is-nexcloud/")
                ),
                ShortVideo(
                    duration = "۰۰:۵۷",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/01/nooshdaroo_69690bea5b429.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/smartphone-security/malware-or-international-internet/")
                ),
                ShortVideo(
                    duration = "null",
                    posterUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2025/06/nooshdaroo_68550a04e843f.webp"),
                    videoUrl = Url("https://nooshdaroo.ir/short-videos/digital-literacy/super-power-saving-in-iphone/")
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
                        subhead = "میان‌برهای پرهزینه …",
                        title = "آشنایی با «هوش مصنوعی سایه»: وقتی کارمندان قوانین امنیتی را دور می‌زنند!"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/legal-guides/shadow-ai-security-risks/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d3fac56689d-768x432.webp"),
                    description = Description(
                        subhead = "بحران انرژی در راه است؟",
                        title = "چرا گوگل، متا و مایکروسافت در حال ساخت نیروگاه‌های گازی هستند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/ai-data-centers-natural-gas/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d350ee31650-768x432.webp"),
                    description = Description(
                        subhead = "روی تاریک و پیش‌بینی‌نشده‌ی هوش مصنوعی",
                        title = "وقتی هوش مصنوعی برای محافظت از هم‌نوع خود، در برابر انسان‌ می‌ایستد!"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/news-opinion/ai-defying-human-commands/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/04/nooshdaroo_69d0dc98b0cbe-768x432.webp"),
                    description = Description(
                        subhead = "انقلاب جدید گوگل در روزهای خاموشی اینترنت ایران!",
                        title = "گوگل Gemma 4 را معرفی کرد؛ مدل‌های متن‌باز رایگان با امکان اجرای آفلاین"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/news-opinion/google-gemma4/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c9102153f3d-768x432.webp"),
                    description = Description(
                        subhead = "«توجه! توجه! توجه!»",
                        title = "درباره ایستگاه رادیویی مرموزی که همزمان با جنگ شروع به پخش کرد چه می‌دانیم؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/numbers-station-during-iran-usa-war/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c46c12443ce-768x432.webp"),
                    description = Description(
                        subhead = "کرک‌هایی خطرناک‌تر از همیشه",
                        title = "توصیه به گیمرها: به‌هیچ‌وجه سراغ نسل جدید بازی‌های کرکی (Hypervisor) نروید"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/parental-control/hypervisor-cracks-dangers/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c8fd8b65b-768x432.avif"),
                    description = Description(
                        subhead = "شبکه‌ای که می‌بیند و حس می‌کند …",
                        title = "آشنایی با شبکه 6G: نسل بعدی اینترنت موبایل"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/6g-explained/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3c0655dfc2-768x432.webp"),
                    description = Description(
                        subhead = "مشکلِ قابل ‌پیش‌بینی بودن!",
                        title = "چرا پسوردهای ساده خیلی راحت فاش می‌شوند؟"
                    ),
                    articleUrl = Url("https://nooshdaroo.ir/digital-literacy/why-simple-passwords-get-hacked-easily/")
                ),
                Article(
                    imageUrl = Url("https://nooshdaroo.ir/wp-content/uploads/2026/03/nooshdaroo_69c3a24690722-768x432.webp"),
                    description = Description(
                        subhead = "اتفاقی آنقدر عادی که دیگر برایمان عجیب نیست!",
                        title = "معجزه در کف اقیانوس: اطلاعاتِ اینترنتی چطور در کسری از ثانیه جابه‌جا می‌شوند؟"
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
                    "خبر و تحلیل",
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
                    "خبر و تحلیل",
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
                    "خبر و تحلیل",
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
                    "بحران و شرایط اضطراری",
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
                    "بحران و شرایط اضطراری",
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
                    "بحران و شرایط اضطراری",
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
                    "بحران و شرایط اضطراری",
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
                    "بحران و شرایط اضطراری",
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
