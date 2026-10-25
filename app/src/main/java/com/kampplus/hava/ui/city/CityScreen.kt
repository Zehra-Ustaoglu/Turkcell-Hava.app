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

@Composable
fun CityScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        items(CityCatalog.cities) { city ->
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
