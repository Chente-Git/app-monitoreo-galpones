package com.duoc.galpones.domain.model

data class Galpon(
    val id: Int,
    val granja: String,
    val nombre: String,
    val tempMin: Double,
    val tempMax: Double,
    val humMin: Double,
    val humMax: Double
)