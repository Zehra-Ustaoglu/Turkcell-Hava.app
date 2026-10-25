package com.kampplus.hava.ui.city

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kampplus.hava.ui.LoadState

@Composable
fun CityScreen(
    cityViewModel: CityViewModel = viewModel()
) {
    val query = cityViewModel.query.collectAsState()
    val selectedCityId = cityViewModel.selectedCityId.collectAsState()
    val uiState = cityViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Şehrini keşfet",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Dünyanın şehirlerini keşfet.",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = query.value,
            onValueChange = { cityViewModel.onQueryChange(it) },
            label = { Text("Şehir ara") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { cityViewModel.search() },
            enabled = query.value.isNotBlank() &&
                uiState.value != LoadState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ara")
        }

        when (val state = uiState.value) {
            LoadState.Loading -> {
                CircularProgressIndicator()
                Text("Şehirler aranıyor…")
            }

            is LoadState.Success -> {
                Text("${state.data.size} şehir bulundu")

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.data,
                        key = { city -> city.id }
                    ) { city ->
                        ApiCityCard(
                            city = city,
                            isSelected = selectedCityId.value == city.id,
                            onClick = {
                                cityViewModel.toggleSelection(city.id)
                            }
                        )
                    }
                }
            }

            LoadState.Empty -> {
                Text("Bir şehir adı yazıp Ara düğmesine dokun.")
            }

            is LoadState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = { cityViewModel.search() }
                ) {
                    Text("Tekrar dene")
                }
            }
        }
    }
}
