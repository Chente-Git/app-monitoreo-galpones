package com.duoc.galpones.domain.model

/** Semáforo del galpón, de menos a más grave. El orden sirve para elegir el peor estado. */
enum class EstadoGalpon {
    NORMAL,
    ADVERTENCIA,
    CRITICO
}