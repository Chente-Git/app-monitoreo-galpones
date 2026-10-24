package com.duoc.galpones.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorObservacionTest {

    @Test
    fun `observacion correcta es valida`() {
        assertTrue(ValidadorObservacion.validar(2, "Ventilador con ruido extraño").esValido)
    }

    @Test
    fun `descripcion vacia da error`() {
        assertEquals("Describe lo que observaste", ValidadorObservacion.validarDescripcion("   "))
    }

    @Test
    fun `descripcion corta da error`() {
        assertNotNull(ValidadorObservacion.validarDescripcion("muy corta"))
    }

    @Test
    fun `descripcion de 10 caracteres es valida`() {
        assertNull(ValidadorObservacion.validarDescripcion("1234567890"))
    }

    @Test
    fun `descripcion sobre 200 caracteres da error`() {
        assertNotNull(ValidadorObservacion.validarDescripcion("a".repeat(201)))
    }

    @Test
    fun `sin galpon seleccionado da error`() {
        assertEquals("Selecciona un galpón", ValidadorObservacion.validar(null, "Descripción válida").errorGalpon)
    }
}
