package com.neofinancial.neo.android.interview.models

import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.Instant

@Serializable
data class Launch(
    val slug: String,
    val name: String,
    var launchDate: String,
    val rocketType: String,
    val organization: String
) {

    fun getTimeInSystemTimeZone(): ZonedDateTime? {
        return try {
            val zonedDateTime = ZonedDateTime.parse(this.launchDate)
            val systemTimeZone = ZoneId.systemDefault()
            zonedDateTime.withZoneSameInstant(systemTimeZone)
        } catch (e: Exception) {
            null
        }
    }

    fun timeUntilLaunch(): Duration {
        return Duration.between(Instant.now(), getTimeInSystemTimeZone()?.toInstant())
    }
}