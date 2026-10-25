package com.duoc.galpones.data.fakeapi

import com.duoc.galpones.domain.model.Rol
import com.duoc.galpones.domain.model.Usuario
import kotlinx.coroutines.delay

/** Usuarios ficticios, uno por perfil, que alimentan el formulario de inicio de sesión. */
object FakeUsuariosApi {

    suspend fun obtenerUsuarios(): List<Usuario> {
        delay(800)
        return listOf(
            Usuario(id = 1, nombre = "Carlos Rojas", correo = "operario@galpones.cl",
                clave = "Operario123", rol = Rol.OPERARIO),
            Usuario(id = 2, nombre = "María Soto", correo = "supervisor@galpones.cl",
                clave = "Supervisor123", rol = Rol.SUPERVISOR),
            Usuario(id = 3, nombre = "Andrés Vidal", correo = "jefatura@galpones.cl",
                clave = "Jefatura123", rol = Rol.JEFATURA)
        )
    }
}