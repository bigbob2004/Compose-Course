package org.example.project

import kotlinx.serialization.Serializable

@Serializable
data class ParkData(
    val title: String,
    val info: String
)