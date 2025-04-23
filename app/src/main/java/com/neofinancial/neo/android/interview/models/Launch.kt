package com.neofinancial.neo.android.interview.models

import kotlinx.serialization.Serializable

@Serializable
data class Launch(
    val name: String,
    val launchDate: String,
)