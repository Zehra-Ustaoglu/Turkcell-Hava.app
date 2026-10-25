package com.kampplus.hava.ui.city

import com.kampplus.hava.model.City
import com.kampplus.hava.model.Forecast
import com.kampplus.hava.ui.LoadState

data class VillageWeather(val city: City, val forecast: LoadState<Forecast>)
