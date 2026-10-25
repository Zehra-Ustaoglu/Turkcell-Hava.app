package com.kampplus.hava.ui.city

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kampplus.hava.R
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.LoadState
import com.kampplus.hava.ui.component.EmptyView
import com.kampplus.hava.ui.component.ErrorView
import com.kampplus.hava.ui.component.FarmBanner
import com.kampplus.hava.ui.component.ShimmerList
import com.kampplus.hava.ui.component.VillageJournal
import com.kampplus.hava.ui.theme.HavaTheme

/**
 * Şehir arama ekranının "bağlayıcı" katmanı.
 *
 * Tek işi ViewModel'i edinip state'i toplamak ve [CityScreen]'e aktarmaktır.
 * Bu ayrım sayesinde [CityScreen] ViewModel tanımaz: @Preview'da ve testte
 * elle state verilerek çalıştırılabilir.
 *
 * `collectAsStateWithLifecycle`: StateFlow'u Compose state'ine
 * çevirir ve uygulama arka plandayken dinlemeyi durdurur.
 */
@Composable
fun CityRoute(onCityClick: (City) -> Unit, modifier: Modifier = Modifier, viewModel: CityViewModel = viewModel()) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCityId by viewModel.selectedCityId.collectAsStateWithLifecycle()
    val onlyTurkishProvinces by viewModel.onlyTurkishProvinces.collectAsStateWithLifecycle()
    val villageWeather by viewModel.villageWeather.collectAsStateWithLifecycle()
    val cities = (uiState as? LoadState.Success)?.data.orEmpty()
    val currentCity = cities.firstOrNull { it.id == selectedCityId } ?: cities.firstOrNull()
    LaunchedEffect(currentCity) {
        if (currentCity == null) viewModel.clearVillage() else viewModel.loadVillage(currentCity)
    }

    CityScreen(
        query = query,
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::search,
        onCityClick = { city ->
            viewModel.selectCity(city)
            onCityClick(city)
        },
        selectedCityId = selectedCityId,
        onlyTurkishProvinces = onlyTurkishProvinces,
        onProvinceFilterChange = viewModel::onProvinceFilterChange,
        villageWeather = villageWeather,
        onRetryVillage = viewModel::retryVillage,
        modifier = modifier
    )
}

/**
 * Şehir arama ekranı: `when` ile her duruma bir görünüm.
 *
 * State tutmaz, yalnızca aldığı değerleri çizer ve olayları yukarı bildirir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    query: String,
    uiState: LoadState<List<City>>,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onCityClick: (City) -> Unit,
    modifier: Modifier = Modifier,
    selectedCityId: Long? = null,
    onlyTurkishProvinces: Boolean = false,
    onProvinceFilterChange: (Boolean) -> Unit = {},
    villageWeather: VillageWeather? = null,
    onRetryVillage: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge)
                        Text("GÖKYÜZÜNDEN KASABANA", style = MaterialTheme.typography.labelSmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Yalnızca Türkiye illeri")
                Switch(checked = onlyTurkishProvinces, onCheckedChange = onProvinceFilterChange)
            }
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "landscape") {
                    val code = (villageWeather?.forecast as? LoadState.Success)?.data?.current?.weatherCode ?: 0
                    FarmBanner(modifier = Modifier.fillMaxWidth(), weatherCode = code)
                }
                villageWeather?.let { weather ->
                    item(key = "journal") { VillageJournal(weather = weather, onRetry = onRetryVillage) }
                }
                when (uiState) {
                    LoadState.Loading -> item(key = "loading") { ShimmerList(itemCount = 3) }
                    is LoadState.Success -> {
                        item(key = "count") {
                            Text("${uiState.data.size} şehir · Keşfe çık", style = MaterialTheme.typography.labelLarge)
                        }
                        items(uiState.data, key = { "city-${it.id}" }) { city ->
                            CityCard(city = city, onClick = { onCityClick(city) }, isSelected = city.id == selectedCityId)
                        }
                    }
                    LoadState.Empty -> item(key = "empty") {
                        EmptyView(title = "Şehir bulunamadı", message = "Bir şehir adı ya da 01–81 arasında plaka kodu dene.")
                    }
                    is LoadState.Error -> item(key = "error") { ErrorView(message = uiState.message, onRetry = onSearch) }
                }
            }
        }
    }
}

/**
 * Arama kutusu.
 *
 * State tutmaz: değeri yukarıdan alır, her değişikliği yukarıya bildirir
 * ("değer aşağı, olay yukarı" — state hoisting).
 */
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit, modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            // Temizle düğmesi yalnızca kutu doluyken görünür.
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.action_clear))
                }
            }
        },
        singleLine = true,
        // Klavyedeki Enter tuşu "Ara" olur.
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch()
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CityScreenPreview() {
    HavaTheme {
        CityScreen(
            query = "Ankara",
            uiState = LoadState.Success(
                listOf(
                    City(323786, "Ankara", 39.91987, 32.85427, "TR", "Türkiye", "Ankara"),
                    City(738648, "Ankara", 41.0050, 39.7269, "TR", "Türkiye", "Trabzon")
                )
            ),
            onQueryChange = {},
            onSearch = {},
            onCityClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CityScreenEmptyPreview() {
    HavaTheme {
        CityScreen(
            query = "xyz",
            uiState = LoadState.Empty,
            onQueryChange = {},
            onSearch = {},
            onCityClick = {}
        )
    }
}
