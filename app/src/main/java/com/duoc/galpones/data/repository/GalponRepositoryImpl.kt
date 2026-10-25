package com.duoc.galpones.data.repository

import com.duoc.galpones.data.fakeapi.FakeGalponesApi
import com.duoc.galpones.data.local.dao.GalponDao
import com.duoc.galpones.data.local.dao.MedicionDao
import com.duoc.galpones.data.mapper.toDomain
import com.duoc.galpones.data.mapper.toEntity
import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.model.Medicion
import com.duoc.galpones.domain.repository.GalponRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Fuente de galpones y mediciones: la UI lee de Room y la FakeApi solo llena tablas vacías. */
class GalponRepositoryImpl(
    private val galponDao: GalponDao,
    private val medicionDao: MedicionDao,
    private val api: FakeGalponesApi
) : GalponRepository {

    /** Evita que dos llamadas a la vez carguen los datos iniciales duplicados. */
    private val mutex = Mutex()

    override fun observarGalpones(): Flow<List<Galpon>> =
        galponDao.observarTodos().map { lista -> lista.map { it.toDomain() } }

    override fun observarGalpon(id: Int): Flow<Galpon?> =
        galponDao.observarPorId(id).map { it?.toDomain() }

    override fun observarUltimasMediciones(): Flow<List<Medicion>> =
        medicionDao.observarUltimas().map { lista -> lista.map { it.toDomain() } }

    override fun observarUltimaMedicion(galponId: Int): Flow<Medicion?> =
        medicionDao.observarUltima(galponId).map { it?.toDomain() }

    override fun observarMediciones(galponId: Int?): Flow<List<Medicion>> {
        // Sin galpón elegido se devuelve el historial de todos.
        val origen = if (galponId == null) {
            medicionDao.observarTodas()
        } else {
            medicionDao.observarPorGalpon(galponId)
        }
        return origen.map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun actualizarUmbrales(
        id: Int,
        tempMin: Double,
        tempMax: Double,
        humMin: Double,
        humMax: Double
    ) = galponDao.actualizarUmbrales(id, tempMin, tempMax, humMin, humMax)

    override suspend fun cargarInicialesSiVacio() {
        mutex.withLock {
            // Se revisa dentro del candado: la segunda llamada ya encuentra la tabla llena.
            if (galponDao.contar() == 0) {
                galponDao.insertarTodos(api.obtenerGalpones().map { it.toEntity() })
            }
            if (medicionDao.contar() == 0) {
                medicionDao.insertarTodas(api.obtenerMediciones().map { it.toEntity() })
            }
        }
    }
}
