package com.neofinancial.neo.android.interview.network

import com.neofinancial.neo.android.interview.models.LaunchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object LaunchesDomain {

    private val client = HttpClient() {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

     suspend fun getLaunches() = client
        .get("https://interview-api.neofinancial.dev/api/launches")
        .body<LaunchResponse>()
        .result
}