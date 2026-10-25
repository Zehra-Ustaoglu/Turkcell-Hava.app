package com.kampplus.hava.ui.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.kampplus.hava.data.CityRepository
import com.kampplus.hava.model.ApiCity
import com.kampplus.hava.ui.LoadState
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CityViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val query: StateFlow<String> =
        savedStateHandle.getStateFlow("query", "")

    val selectedCityId: StateFlow<Long?> =
        savedStateHandle.getStateFlow<Long?>("selectedCityId", null)

    fun onQueryChange(newQuery: String) {
        savedStateHandle["query"] = newQuery
    }

    fun toggleSelection(cityId: Long) {
        savedStateHandle["selectedCityId"] =
            if (selectedCityId.value == cityId) {
                null
            } else {
                cityId
            }
    }

    fun search() {
        val searchText = query.value.trim()

        searchJob?.cancel()

        if (searchText.isBlank()) {
            _uiState.value = LoadState.Empty
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.value = LoadState.Loading

            try {
                val cities = repository.search(searchText)

                _uiState.value = if (cities.isEmpty()) {
                    LoadState.Empty
                } else {
                    LoadState.Success(cities)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.value =
                    LoadState.Error("İnternet bağlantını kontrol et.")
            } catch (e: HttpException) {
                _uiState.value =
                    LoadState.Error("Sunucu şu an cevap vermiyor.")
            } catch (e: Exception) {
                _uiState.value =
                    LoadState.Error("Bir şeyler ters gitti. Tekrar dene.")
            }
        }
    }

    fun clearQuery() {
        savedStateHandle["query"] = ""
    }

    private val repository = CityRepository()

    private val _uiState =
        MutableStateFlow<LoadState<List<ApiCity>>>(LoadState.Empty)

    val uiState = _uiState.asStateFlow()  // _uiState viewModel in değiştirebildiği durum
// uiState ekranın koruyabileceği durum

    private var searchJob: Job? = null  // searchJob devam eden aramayı takip eder
}
