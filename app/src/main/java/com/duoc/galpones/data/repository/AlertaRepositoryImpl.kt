package com.duoc.galpones.data.repository

import com.duoc.galpones.data.fakeapi.FakeAlertasApi
import com.duoc.galpones.data.local.dao.AlertaDao
import com.duoc.galpones.data.mapper.toDomain
import com.duoc.galpones.data.mapper.toEntity
import com.duoc.galpones.domain.model.Alerta
import com.duoc.galpones.domain.model.EstadoAlerta
import com.duoc.galpones.domain.repository.AlertaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AlertaRepositoryImpl(
    private val dao: AlertaDao,
    private val api: FakeAlertasApi
) : AlertaRepository {

    private val mutex = Mutex()
    override fun observarTodas(): Flow<List<Alerta>> =
        dao.observarTodas().map { lista -> lista.map { it.toDomain() } }

    override fun observarActivas(): Flow<List<Alerta>> =
        dao.observarActivas(EstadoAlerta.RESUELTA.name).map { lista -> lista.map { it.toDomain() } }

    override fun contarPorEstado(estado: EstadoAlerta): Flow<Int> =
        dao.contarPorEstado(estado.name)

    override suspend fun obtenerPorId(id: Long): Alerta? =
        dao.obtenerPorId(id)?.toDomain()

    override suspend fun actualizarEstado(id: Long, estado: EstadoAlerta) =
        dao.actualizarEstado(id, estado.name)

    override suspend fun cargarInicialesSiVacio() {
        mutex.withLock {
            if (dao.contar() == 0) {
                val alertas = api.obtenerAlertas()
                dao.insertarTodas(alertas.map { it.toEntity() })
            }
        }
    }
}