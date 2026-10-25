package com.kampplus.hava.ui.city

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kampplus.hava.CityCard
import com.kampplus.hava.data.CityCatalog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextButton

@Composable
fun CityScreen() {
    val query = remember { mutableStateOf("") }  // Başlangıçta boş olan, değiştiğinde ekranı güncelleyen bir değer oluşturur.
    val searchText = query.value.trim()

    val filteredCities = CityCatalog.cities.filter { city ->
        city.name.contains(searchText, ignoreCase = true) ||
            city.plate.toString().padStart(2, '0').contains(searchText)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Şehrini keşfet",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Bir sonraki durağını bul.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = query.value,
            onValueChange = { newText ->
                query.value = newText
            },
            label = { Text("Şehir veya plaka ara") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "${filteredCities.size} şehir bulundu",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 12.dp)
        )

        if (filteredCities.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text(
                    text = "Şehir bulunamadı",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Başka bir şehir adı veya plaka kodu dene.",
                    modifier = Modifier.padding(top = 8.dp)
                )

                TextButton(
                    onClick = {
                        query.value = ""
                    }
                ) {
                    Text("Aramayı temizle")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 12.dp)
            ) {
                items(filteredCities) { city ->
                    CityCard(
                        name = city.name,
                        plate = city.plate,
                        onClick = {
                            Log.d("CityClick", "Tıklandı: ${city.name}")
                        }
                    )
                }
            }
        }
    }
}
