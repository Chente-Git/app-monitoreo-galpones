package com.duoc.galpones.domain.model

/** Galpón con los rangos (umbrales) de temperatura y humedad que se consideran normales. */
data class Galpon(
    val id: Int,
    val granja: String,
    val nombre: String,
    val tempMin: Double,
    val tempMax: Double,
    val humMin: Double,
    val humMax: Double
)