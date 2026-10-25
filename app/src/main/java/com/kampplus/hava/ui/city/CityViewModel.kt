package com.kampplus.hava.ui.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class CityViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val query: StateFlow<String> =
        savedStateHandle.getStateFlow("query", "")

    val selectedPlate: StateFlow<Int?> =
        savedStateHandle.getStateFlow<Int?>("selectedPlate", null)

    fun onQueryChange(newQuery: String) {
        savedStateHandle["query"] = newQuery
    }

    fun toggleSelection(plate: Int) {
        savedStateHandle["selectedPlate"] =
            if (selectedPlate.value == plate) {
                null
            } else {
                plate
            }
    }

    fun clearQuery() {
        savedStateHandle["query"] = ""
    }
}
