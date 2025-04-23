package com.neofinancial.neo.android.interview.models

import kotlinx.serialization.Serializable

@Serializable
data class LaunchResponse(
    val result: List<Launch>
)