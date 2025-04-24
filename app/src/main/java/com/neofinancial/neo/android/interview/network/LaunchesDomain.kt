package com.neofinancial.neo.android.interview.network

import com.neofinancial.neo.android.interview.models.LaunchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object LaunchesDomain {

    private const val BASE_URL = "https://interview-api.neofinancial.dev/api/launches"
    private const val PAGE_SIZE = 10

    private val client = HttpClient {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    suspend fun getLaunches(page: Int): LaunchResponse {
        val offset = (page - 1) * PAGE_SIZE
        return client
            .get("$BASE_URL?limit=$PAGE_SIZE&offset=$offset")
            .body<LaunchResponse>()
    }
}