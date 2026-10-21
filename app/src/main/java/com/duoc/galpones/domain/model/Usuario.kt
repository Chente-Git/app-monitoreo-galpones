package com.duoc.galpones.domain.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: Rol,
    val telefono: String = ""
)
