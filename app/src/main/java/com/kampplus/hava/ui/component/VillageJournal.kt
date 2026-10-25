package com.kampplus.hava.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kampplus.hava.model.FarmAdvice
import com.kampplus.hava.model.WeatherCondition
import com.kampplus.hava.ui.LoadState
import com.kampplus.hava.ui.city.VillageWeather
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun VillageJournal(weather: VillageWeather, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Bugün kasabanda", style = MaterialTheme.typography.titleMedium)
                Text(weather.city.name, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                when (val state = weather.forecast) {
                    LoadState.Loading -> Row(verticalAlignment = Alignment.CenterVertically) {
                        GrowingPlant()
                        Text("Kasabadan haber geliyor…", style = MaterialTheme.typography.bodyMedium)
                    }
                    is LoadState.Success -> {
                        val current = state.data.current
                        val advice = FarmAdvice.from(state.data)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            PixelWeatherIcon(code = current.weatherCode)
                            Column {
                                Text(
                                    "${current.temperatureC.roundToInt()}° · ${WeatherCondition.fromWeatherCode(
                                        current.weatherCode
                                    ).label}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "Şehir saatiyle ${current.observedAt.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        Text(advice.story, style = MaterialTheme.typography.bodyMedium)
                    }
                    else -> {
                        Text("Kasabanın hava bilgisine şu an ulaşamadık.", style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = onRetry) { Text("Tekrar dene") }
                    }
                }
            }
        }
        val forecast = weather.forecast
        if (forecast is LoadState.Success) {
            Card(border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Çiftlik hazırlığı", style = MaterialTheme.typography.titleMedium)
                    FarmAdvice.from(forecast.data).preparations.forEach { tip ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("◆", color = MaterialTheme.colorScheme.primary)
                            Text(tip, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
