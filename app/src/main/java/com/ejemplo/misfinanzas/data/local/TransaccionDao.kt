package com.ejemplo.misfinanzas.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ejemplo.misfinanzas.data.model.Transaccion

// @Dao marca esta interfaz como un Data Access Object
// Room genera la implementación automáticamente en tiempo de compilación
@Dao
interface TransaccionDao {

    // @Query ejecuta una consulta SQL personalizada
    // Retorna LiveData: la UI se actualiza automáticamente cuando la tabla cambia
    // Las funciones que retornan LiveData NO necesitan ser "suspend"
    // porque Room las ejecuta en segundo plano automáticamente
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun obtenerTodas(): LiveData<List<Transaccion>>

    // Consulta que filtra solo ingresos (monto > 0)
    @Query("SELECT * FROM transacciones WHERE monto > 0 ORDER BY fecha DESC")
    fun obtenerIngresos(): LiveData<List<Transaccion>>

    // Consulta que filtra solo gastos (monto < 0)
    @Query("SELECT * FROM transacciones WHERE monto < 0 ORDER BY fecha DESC")
    fun obtenerGastos(): LiveData<List<Transaccion>>

    // Consulta que suma todos los montos (balance total)
    // COALESCE retorna 0.0 si no hay registros (evita null)
    @Query("SELECT COALESCE(SUM(monto), 0.0) FROM transacciones")
    fun obtenerBalance(): LiveData<Double>

    // Consulta que suma solo los ingresos
    @Query("SELECT COALESCE(SUM(monto), 0.0) FROM transacciones WHERE monto > 0")
    fun obtenerTotalIngresos(): LiveData<Double>

    // Consulta que suma solo los gastos (valor absoluto)
    @Query("SELECT COALESCE(SUM(ABS(monto)), 0.0) FROM transacciones WHERE monto < 0")
    fun obtenerTotalGastos(): LiveData<Double>

    // Cuenta el número total de transacciones
    @Query("SELECT COUNT(*) FROM transacciones")
    fun obtenerCantidad(): LiveData<Int>

    // @Insert inserta un registro en la tabla
    // "suspend" indica que esta función se ejecuta en una coroutine (segundo plano)
    // Las operaciones de escritura DEBEN ser suspend para no bloquear el hilo principal
    @Insert
    suspend fun insertar(transaccion: Transaccion)

    // Inserta múltiples registros de una vez
    @Insert
    suspend fun insertarTodas(transacciones: List<Transaccion>)

    // @Update actualiza un registro existente (busca por primary key)
    @Update
    suspend fun actualizar(transaccion: Transaccion)

    // @Delete elimina un registro (busca por primary key)
    @Delete
    suspend fun eliminar(transaccion: Transaccion)

    // Elimina todos los registros de la tabla
    @Query("DELETE FROM transacciones")
    suspend fun eliminarTodas()
}