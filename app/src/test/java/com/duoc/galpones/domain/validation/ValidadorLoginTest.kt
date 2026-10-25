package com.duoc.galpones.domain.validation

import com.duoc.galpones.domain.model.Rol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorLoginTest {

    @Test
    fun `login correcto es valido`() {
        val r = ValidadorLogin.validar("operario@galpones.cl", "Operario123", Rol.OPERARIO)
        assertTrue(r.esValido)
    }

    @Test
    fun `correo vacio da error`() {
        assertEquals("Ingresa tu correo", ValidadorLogin.validarCorreo(""))
    }

    @Test
    fun `correo con formato invalido da error`() {
        assertNotNull(ValidadorLogin.validarCorreo("no-es-un-correo"))
        assertNotNull(ValidadorLogin.validarCorreo("falta@dominio"))
    }

    @Test
    fun `correo valido pasa`() {
        assertNull(ValidadorLogin.validarCorreo("usuario@galpones.cl"))
    }

    @Test
    fun `clave vacia da error`() {
        assertEquals("Ingresa tu clave", ValidadorLogin.validarClave(""))
    }

    @Test
    fun `clave corta da error`() {
        assertNotNull(ValidadorLogin.validarClave("abc"))
    }

    @Test
    fun `clave de 8 caracteres es valida`() {
        assertNull(ValidadorLogin.validarClave("12345678"))
    }

    @Test
    fun `perfil nulo da error`() {
        assertEquals("Elige un perfil", ValidadorLogin.validarPerfil(null))
    }

    @Test
    fun `combinacion de errores`() {
        val r = ValidadorLogin.validar("", "abc", null)
        assertNotNull(r.errorCorreo)
        assertNotNull(r.errorClave)
        assertNotNull(r.errorPerfil)
    }
}
