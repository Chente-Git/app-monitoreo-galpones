package com.duoc.galpones.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorUmbralesTest {

    @Test
    fun `valores coherentes son validos`() {
        val r = ValidadorUmbrales.validar(1, "20", "30", "50", "70")
        assertTrue(r.esValido)
    }

    @Test
    fun `acepta coma como separador decimal`() {
        assertEquals(22.5, ValidadorUmbrales.parsear("22,5")!!, 0.0)
    }

    @Test
    fun `campo vacio da error`() {
        val r = ValidadorUmbrales.validar(1, "", "30", "50", "70")
        assertNotNull(r.errorTempMin)
        assertNull(r.errorTempMax)
    }

    @Test
    fun `texto no numerico da error`() {
        val r = ValidadorUmbrales.validar(1, "abc", "30", "50", "70")
        assertEquals("Ingresa un número válido", r.errorTempMin)
    }

    @Test
    fun `NaN e infinito no son numeros validos`() {
        assertNull(ValidadorUmbrales.parsear("NaN"))
        assertNull(ValidadorUmbrales.parsear("Infinity"))
    }

    @Test
    fun `temperatura fuera de rango da error`() {
        val r = ValidadorUmbrales.validar(1, "20", "80", "50", "70")
        assertNotNull(r.errorTempMax)
    }

    @Test
    fun `humedad sobre 100 da error`() {
        val r = ValidadorUmbrales.validar(1, "20", "30", "50", "120")
        assertNotNull(r.errorHumMax)
    }

    @Test
    fun `minimo mayor o igual al maximo da error en el maximo`() {
        val r = ValidadorUmbrales.validar(1, "30", "30", "70", "50")
        assertNotNull(r.errorTempMax)
        assertNotNull(r.errorHumMax)
        assertNull(r.errorTempMin)
        assertNull(r.errorHumMin)
    }

    @Test
    fun `sin galpon seleccionado da error`() {
        val r = ValidadorUmbrales.validar(null, "20", "30", "50", "70")
        assertEquals("Selecciona un galpón", r.errorGalpon)
    }
}
