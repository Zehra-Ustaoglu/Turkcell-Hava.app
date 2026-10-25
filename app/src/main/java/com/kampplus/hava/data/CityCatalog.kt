package com.kampplus.hava.data

import com.kampplus.hava.model.City

data class StaticCity(val name: String, val plate: Int)

object CityCatalog {
    private val coordinates = listOf(
        39.91987 to 32.85427, 41.01384 to 28.94966, 38.41273 to 27.13838,
        40.19559 to 29.06013, 36.90812 to 30.69556, 37.87135 to 32.48464,
        37.00167 to 35.32889, 41.005 to 39.72694, 39.77667 to 30.52056,
        37.05944 to 37.3825
    )

    val cities = listOf(
        StaticCity(name = "Ankara", plate = 6),
        StaticCity(name = "İstanbul", plate = 34),
        StaticCity(name = "İzmir", plate = 35),
        StaticCity(name = "Bursa", plate = 16),
        StaticCity(name = "Antalya", plate = 7),
        StaticCity(name = "Konya", plate = 42),
        StaticCity(name = "Adana", plate = 1),
        StaticCity(name = "Trabzon", plate = 61),
        StaticCity(name = "Eskişehir", plate = 26),
        StaticCity(name = "Gaziantep", plate = 27)
    )

    val apiCities: List<City> = cities.mapIndexed { index, city ->
        City(
            -city.plate.toLong(),
            city.name,
            coordinates[index].first,
            coordinates[index].second,
            "TR",
            "Türkiye",
            city.name,
            "PPLA"
        )
    }
}
