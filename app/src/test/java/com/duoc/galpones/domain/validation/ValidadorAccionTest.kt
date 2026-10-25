package com.duoc.galpones.domain.validation

import com.duoc.galpones.domain.model.TipoAccion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorAccionTest {

    @Test
    fun `accion con todos los campos correctos es valida`() {
        val r = ValidadorAccion.validar(
            alertaId = 10L,
            tipo = TipoAccion.REVISION_VENTILACION,
            descripcion = "Se revisaron los ventiladores del galpón"
        )
        assertTrue(r.esValido)
    }

    @Test
    fun `alerta nula o cero da error`() {
        assertEquals("Selecciona una alerta", ValidadorAccion.validarAlerta(null))
        assertEquals("Selecciona una alerta", ValidadorAccion.validarAlerta(0L))
    }

    @Test
    fun `tipo nulo da error`() {
        assertEquals("Elige un tipo de acción", ValidadorAccion.validarTipo(null))
    }

    @Test
    fun `descripcion en blanco da error`() {
        assertEquals("Describe la acción realizada", ValidadorAccion.validarDescripcion("   "))
    }

    @Test
    fun `descripcion corta da error`() {
        assertNotNull(ValidadorAccion.validarDescripcion("corta"))
    }

    @Test
    fun `descripcion de 10 caracteres es valida`() {
        assertNull(ValidadorAccion.validarDescripcion("1234567890"))
    }
}
