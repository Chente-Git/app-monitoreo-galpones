package com.duoc.galpones.domain.model

/** Etapa de una alerta. Las resueltas dejan de aparecer entre las alertas activas. */
enum class EstadoAlerta {
    PENDIENTE,
    EN_ATENCION,
    RESUELTA
}
