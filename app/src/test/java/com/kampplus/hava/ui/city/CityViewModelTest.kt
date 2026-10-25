package com.kampplus.hava.ui.city

import androidx.lifecycle.SavedStateHandle
import com.kampplus.hava.testing.FakeCityRepository
import com.kampplus.hava.testing.MainDispatcherRule
import com.kampplus.hava.testing.sampleCity
import com.kampplus.hava.ui.LoadState
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * Şehir listesi ekranının dört durumunu doğrular.
 *
 * Temel kural: her istek Loading ile başlar ve
 * Success / Empty / Error'dan biriyle biter.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCityRepository()

    private fun createViewModel() = CityViewModel(repository)

    @Test
    fun `hizli yazarken sadece son sorgu aranir`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()
        val initialCalls = repository.callCount
        vm.onQueryChange("An")
        advanceTimeBy(100)
        vm.onQueryChange("Ankara")
        advanceUntilIdle()
        assertEquals(initialCalls + 1, repository.callCount)
        assertEquals("Ankara", repository.lastQuery)
    }

    @Test
    fun `temizleme bekleyen aramayi iptal eder ve on sabit sehri gosterir`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()
        val initialCalls = repository.callCount
        vm.onQueryChange("Ankara")
        vm.clearQuery()
        advanceUntilIdle()
        assertEquals("", vm.query.value)
        assertEquals(initialCalls, repository.callCount)
        assertEquals(10, (vm.uiState.value as LoadState.Success).data.size)
    }

    @Test
    fun `arama secim ve filtre saved state uzerinden geri yuklenir`() = runTest {
        val saved = SavedStateHandle(mapOf("query" to "İzmir", "selectedCityId" to 35L, "onlyTurkishProvinces" to true))
        val vm = CityViewModel(repository, saved)
        advanceUntilIdle()
        assertEquals("İzmir", vm.query.value)
        assertEquals(35L, vm.selectedCityId.value)
        assertTrue(vm.onlyTurkishProvinces.value)
        assertEquals("İzmir", repository.lastQuery)
    }

    @Test
    fun `baslangicta Loading gosterilir`() = runTest {
        val viewModel = createViewModel()

        // advanceUntilIdle çağrılmadan önce init içindeki arama henüz bitmemiştir.
        assertEquals(LoadState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `acilista otomatik arama yapilir`() = runTest {
        repository.result = listOf(sampleCity())

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(1, repository.callCount)
        assertEquals("İstanbul", viewModel.query.value)
    }

    @Test
    fun `veri gelince Success durumuna gecilir`() = runTest {
        repository.result = listOf(sampleCity(), sampleCity(id = 2L, name = "İzmir"))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Success bekleniyordu, gelen: $state", state is LoadState.Success)
        assertEquals(2, (state as LoadState.Success).data.size)
    }

    @Test
    fun `bos liste Empty durumuna cevrilir`() = runTest {
        // Cevap geldi ama içi boş: bu bir hata değildir.
        repository.result = emptyList()

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(LoadState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `baglanti hatasi anlasilir mesaja cevrilir`() = runTest {
        repository.error = IOException("no network")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Error bekleniyordu, gelen: $state", state is LoadState.Error)
        // Kullanıcıya teknik mesaj değil, insan dilinde bir cümle gösterilir.
        assertEquals("İnternet bağlantını kontrol et.", (state as LoadState.Error).message)
    }

    @Test
    fun `sunucu hatasi farkli mesaj gosterir`() = runTest {
        repository.error = HttpException(
            Response.error<Unit>(500, "".toResponseBody("application/json".toMediaType()))
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(
            "Sunucu şu an cevap vermiyor.",
            (viewModel.uiState.value as LoadState.Error).message
        )
    }

    @Test
    fun `onQueryChange arama metnini gunceller ve otomatik arar`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()
        val callsAfterInit = repository.callCount

        viewModel.onQueryChange("Rize")
        advanceUntilIdle()

        assertEquals("Rize", viewModel.query.value)
        assertEquals(callsAfterInit + 1, repository.callCount)
    }

    @Test
    fun `search guncel metinle istek atar`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onQueryChange("Rize")
        viewModel.search()
        advanceUntilIdle()

        assertEquals("Rize", repository.lastQuery)
    }

    @Test
    fun `tekrar dene yeni bir istek baslatir ve basarili olabilir`() = runTest {
        repository.error = IOException("no network")
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is LoadState.Error)

        // Bağlantı geri geldi.
        repository.error = null
        repository.result = listOf(sampleCity())
        viewModel.search()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoadState.Success)
        assertEquals(2, repository.callCount)
    }
}
