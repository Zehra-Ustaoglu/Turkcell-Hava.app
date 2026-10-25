package com.kampplus.hava.data

import com.kampplus.hava.testing.createApi
import com.kampplus.hava.testing.readResource
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Şehir arama API'sinin gerçek bir JSON yanıtını doğru okuduğunu doğrular.
 *
 * Bu test neden önemli? Gson eşlemesi **sessizce** bozulur: `@SerializedName`
 * unutulursa alan hata vermeden `null` kalır ve hata ancak ekranda fark edilir.
 * Burada gerçek yanıt dosyasıyla eşlemeyi kilitliyoruz.
 */
class CityApiTest {

    @Test
    fun `plaka aramasi Turkiye iliyle sinirlanir ve isim sorgusuna cevrilir`() = runTest {
        val repo = OpenMeteoCityRepository(api, StandardTestDispatcher(testScheduler))
        listOf("6", "06").forEach { query ->
            server.enqueue(
                MockResponse().setBody(
                    """{"results":[
                {"id":1,"name":"Ankara","country_code":"TR","feature_code":"PPLC"},
                {"id":2,"name":"Ankara","country_code":"US","feature_code":"PPLA"},
                {"id":3,"name":"Ankara İlçesi","country_code":"TR","feature_code":"PPLA2"}
            ]}"""
                )
            )
            assertEquals(listOf(1L), repo.search(query, false).map { it.id })
            val url = checkNotNull(server.takeRequest().requestUrl)
            assertEquals("Ankara", url.queryParameter("name"))
            assertEquals("TR", url.queryParameter("countryCode"))
        }
    }

    @Test
    fun `gecersiz plaka APIye gonderilmez`() = runTest {
        val repo = OpenMeteoCityRepository(api, StandardTestDispatcher(testScheduler))
        listOf("00", "82", "99999999999999").forEach { assertTrue(repo.search(it).isEmpty()) }
        assertEquals(0, server.requestCount)
    }

    @Test
    fun `il merkezinin adi farkliysa plaka dogru sehre gider`() = runTest {
        server.enqueue(MockResponse().setBody("""{"results":[{"id":1,"name":"İzmit","country_code":"TR","feature_code":"PPLA"}]}"""))
        val repo = OpenMeteoCityRepository(api, StandardTestDispatcher(testScheduler))
        assertEquals("İzmit", repo.search("41").single().name)
        assertEquals("İzmit", checkNotNull(server.takeRequest().requestUrl).queryParameter("name"))
    }

    @Test
    fun `Turkiye il filtresi ilceleri ve yabanci sehirleri dislar`() = runTest {
        server.enqueue(
            MockResponse().setBody(
                """{"results":[
            {"id":1,"name":"Ankara","country_code":"TR","feature_code":"PPLC"},
            {"id":2,"name":"İzmir","country_code":"TR","feature_code":"PPLA"},
            {"id":3,"name":"Çankaya","country_code":"TR","feature_code":"PPLA2"},
            {"id":4,"name":"Berlin","country_code":"DE","feature_code":"PPLC"}
        ]}"""
            )
        )
        val repo = OpenMeteoCityRepository(api, StandardTestDispatcher(testScheduler))
        assertEquals(listOf("Ankara", "İzmir"), repo.search("  An  ", true).map { it.name })
        val url = checkNotNull(server.takeRequest().requestUrl)
        assertEquals("TR", url.queryParameter("countryCode"))
        assertEquals("An", url.queryParameter("name"))
    }

    @Test
    fun `dunya aramasinda ulke kisitlamasi gonderilmez`() = runTest {
        server.enqueue(MockResponse().setBody(readResource("geocoding_berlin.json")))
        val repo = OpenMeteoCityRepository(api, StandardTestDispatcher(testScheduler))
        assertTrue(repo.search("Berlin").isNotEmpty())
        assertNull(checkNotNull(server.takeRequest().requestUrl).queryParameter("countryCode"))
    }

    private lateinit var server: MockWebServer
    private lateinit var api: CityApi

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        api = server.createApi()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `yanittaki alanlar City modeline dogru eslenir`() = runTest {
        server.enqueue(MockResponse().setBody(readResource("geocoding_berlin.json")))

        val response = api.search("Berlin")
        val berlin = checkNotNull(response.results).first()

        assertEquals(2950159L, berlin.id)
        assertEquals("Berlin", berlin.name)
        assertEquals(52.52437, berlin.latitude, 0.00001)
        assertEquals(13.41053, berlin.longitude, 0.00001)
        // country_code -> countryCode eşlemesi @SerializedName ile yapılır.
        assertEquals("DE", berlin.countryCode)
        assertEquals("Almanya", berlin.country)
        assertEquals("Berlin", berlin.admin1)
    }

    @Test
    fun `eksik alanlar null kalir ve cozumleme cokmez`() = runTest {
        server.enqueue(MockResponse().setBody(readResource("geocoding_berlin.json")))

        // İkinci kayıtta country_code alanı hiç yok.
        val maryland = checkNotNull(api.search("Berlin").results)[1]

        assertNull(maryland.countryCode)
        assertEquals("Maryland", maryland.admin1)
    }

    @Test
    fun `subtitle bolge ve ulkeyi birlestirir`() = runTest {
        server.enqueue(MockResponse().setBody(readResource("geocoding_berlin.json")))

        val berlin = checkNotNull(api.search("Berlin").results).first()

        // admin1 ve country aynı olduğunda tekrar yazılmaz: "Berlin" (", Almanya" eklenir).
        assertEquals("Berlin, Almanya", berlin.subtitle)
    }

    @Test
    fun `sonuc yoksa results alani null gelir`() = runTest {
        // Open-Meteo sonuç bulamazsa "results" anahtarını hiç göndermez.
        server.enqueue(MockResponse().setBody("""{"generationtime_ms":0.4}"""))

        val response = api.search("xyzxyz")

        assertNull(response.results)
        // ViewModel bunu orEmpty() ile boş listeye çevirip Empty durumu üretir.
        assertTrue(response.results.orEmpty().isEmpty())
    }

    @Test
    fun `istek dogru yola ve parametrelerle gider`() = runTest {
        server.enqueue(MockResponse().setBody(readResource("geocoding_berlin.json")))

        api.search("Ankara")

        val request = server.takeRequest()
        val path = checkNotNull(request.path)
        assertTrue("Yol v1/search olmali: $path", path.startsWith("/v1/search"))
        assertTrue("name parametresi eksik: $path", path.contains("name=Ankara"))
        assertTrue("count varsayilani 100 olmali: $path", path.contains("count=100"))
        assertTrue("language varsayilani tr olmali: $path", path.contains("language=tr"))
    }
}
