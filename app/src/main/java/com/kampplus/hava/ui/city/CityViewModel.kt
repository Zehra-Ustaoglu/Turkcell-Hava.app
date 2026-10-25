package com.kampplus.hava.ui.city

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kampplus.hava.data.CityCatalog
import com.kampplus.hava.data.CityRepository
import com.kampplus.hava.data.OpenMeteoCityRepository
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.LoadState
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CityViewModel(
    private val repository: CityRepository = OpenMeteoCityRepository(),
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {
    constructor(savedStateHandle: SavedStateHandle) : this(OpenMeteoCityRepository(), savedStateHandle)

    val query: StateFlow<String> = savedStateHandle.getStateFlow("query", "İstanbul")
    val selectedCityId: StateFlow<Long?> = savedStateHandle.getStateFlow("selectedCityId", null)
    val onlyTurkishProvinces: StateFlow<Boolean> = savedStateHandle.getStateFlow("onlyTurkishProvinces", false)
    private val _uiState = MutableStateFlow<LoadState<List<City>>>(LoadState.Loading)
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        search()
    }

    fun onQueryChange(newQuery: String) {
        savedStateHandle["query"] = newQuery
        scheduleSearch(350)
    }

    fun onProvinceFilterChange(enabled: Boolean) {
        savedStateHandle["onlyTurkishProvinces"] = enabled
        search()
    }

    fun selectCity(city: City) {
        savedStateHandle["selectedCityId"] = city.id
        Log.d("CityScreen", "Tıklandı: ${city.name}")
    }

    fun clearQuery() = onQueryChange("")
    fun search() = scheduleSearch(0)

    private fun scheduleSearch(waitMillis: Long) {
        searchJob?.cancel()
        val text = query.value.trim()
        if (text.isEmpty()) {
            _uiState.value = LoadState.Success(CityCatalog.apiCities)
            return
        }
        searchJob = viewModelScope.launch {
            // Remove stale results while waiting for the next query.
            _uiState.value = LoadState.Loading
            delay(waitMillis)
            _uiState.value = try {
                val cities = repository.search(text, onlyTurkishProvinces.value)
                if (cities.isEmpty()) LoadState.Empty else LoadState.Success(cities)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                LoadState.Error("İnternet bağlantını kontrol et.")
            } catch (e: HttpException) {
                LoadState.Error("Sunucu şu an cevap vermiyor.")
            } catch (e: Exception) {
                Log.e("CityViewModel", "Arama başarısız", e)
                LoadState.Error("Bir şeyler ters gitti. Tekrar dene.")
            }
        }
    }
}
