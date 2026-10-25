package com.kampplus.hava.data

import com.kampplus.hava.model.ApiCity

class CityRepository(
    private val cityApi: CityApi = ApiClient.cityApi
) {

    suspend fun search(query: String): List<ApiCity> {
        val response = cityApi.search(name = query.trim())

        return response.results.orEmpty()
    }
}
