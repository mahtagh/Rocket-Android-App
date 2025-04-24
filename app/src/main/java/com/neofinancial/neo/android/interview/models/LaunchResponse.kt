package com.neofinancial.neo.android.interview.models

import kotlinx.serialization.Serializable

@Serializable
data class LaunchResponse(
    val meta: Meta,
    val result: List<Launch>
)

@Serializable
data class Meta(
    val total: Int,
    val limit: Int
)