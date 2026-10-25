package com.duoc.galpones.domain.model

/**
 * Usuario del sistema.
 *
 * TODO (producción): la clave viaja y se compara en texto plano desde la FakeApi;
 * en una implementación real debería guardarse como hash (por ejemplo BCrypt o Argon2)
 * y nunca mantenerse en memoria más de lo necesario.
 */
data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: Rol,
    val telefono: String = ""
)
