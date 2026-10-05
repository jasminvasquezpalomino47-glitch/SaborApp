package com.vasquez.saborapp.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vasquez.saborapp.model.DetallePedido
import com.vasquez.saborapp.model.Pedido
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidoDao(private val dbHelper: DBHelper) {

    private fun getFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    fun obtenerPedidoAbiertoPorMesa(idMesa: Int): Pedido? {
        val db = dbHelper.readableDatabase
        val query = "SELECT ${DBHelper.PED_ID}, ${DBHelper.PED_ID_MESA}, ${DBHelper.PED_FECHA}, ${DBHelper.PED_ESTADO}, ${DBHelper.PED_TOTAL} " +
                "FROM ${DBHelper.TABLA_PEDIDO} WHERE ${DBHelper.PED_ID_MESA} = ? AND ${DBHelper.PED_ESTADO} = 'ABIERTO'"
        val cursor = db.rawQuery(query, arrayOf(idMesa.toString()))

        var pedido: Pedido? = null
        if (cursor.moveToFirst()) {
            pedido = Pedido(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PED_ID)),
                idMesa = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PED_ID_MESA)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PED_FECHA)),
                estado = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PED_ESTADO)),
                total = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.PED_TOTAL))
            )
        }
        cursor.close()
        return pedido
    }

    fun obtenerOCrearPedidoAbierto(idMesa: Int): Pedido {
        var pedido = obtenerPedidoAbiertoPorMesa(idMesa)
        if (pedido == null) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DBHelper.PED_ID_MESA, idMesa)
                put(DBHelper.PED_FECHA, getFechaActual())
                put(DBHelper.PED_ESTADO, "ABIERTO")
                put(DBHelper.PED_TOTAL, 0.0)
            }
            val id = db.insert(DBHelper.TABLA_PEDIDO, null, values).toInt()
            pedido = Pedido(id = id, idMesa = idMesa, fecha = getFechaActual(), estado = "ABIERTO", total = 0.0)
        }
        return pedido
    }

    fun agregarPlatoAPedido(idPedido: Int, idMesa: Int, idPlato: Int, cantidad: Int, precioUnit: Double): Boolean {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        return try {
            val subtotal = cantidad * precioUnit

            // Verificar si el plato ya está en el detalle del pedido
            val checkQuery = "SELECT ${DBHelper.DET_ID}, ${DBHelper.DET_CANTIDAD} FROM ${DBHelper.TABLA_DETALLE} WHERE ${DBHelper.DET_ID_PEDIDO} = ? AND ${DBHelper.DET_ID_PLATO} = ?"
            val cursor = db.rawQuery(checkQuery, arrayOf(idPedido.toString(), idPlato.toString()))

            if (cursor.moveToFirst()) {
                val idDetalle = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DET_ID))
                val cantExistente = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DET_CANTIDAD))
                val nuevaCant = cantExistente + cantidad
                val nuevoSubtotal = nuevaCant * precioUnit

                val values = ContentValues().apply {
                    put(DBHelper.DET_CANTIDAD, nuevaCant)
                    put(DBHelper.DET_SUBTOTAL, nuevoSubtotal)
                }
                db.update(DBHelper.TABLA_DETALLE, values, "${DBHelper.DET_ID} = ?", arrayOf(idDetalle.toString()))
            } else {
                val values = ContentValues().apply {
                    put(DBHelper.DET_ID_PEDIDO, idPedido)
                    put(DBHelper.DET_ID_PLATO, idPlato)
                    put(DBHelper.DET_CANTIDAD, cantidad)
                    put(DBHelper.DET_PRECIO_UNIT, precioUnit)
                    put(DBHelper.DET_SUBTOTAL, subtotal)
                }
                db.insert(DBHelper.TABLA_DETALLE, null, values)
            }
            cursor.close()

            // Recalcular total del pedido
            recalcularTotalInt(db, idPedido)

            // Cambiar mesa a OCUPADA
            val mesaValues = ContentValues().apply { put(DBHelper.M_ESTADO, "OCUPADA") }
            db.update(DBHelper.TABLA_MESA, mesaValues, "${DBHelper.M_ID} = ?", arrayOf(idMesa.toString()))

            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
        }
    }

    fun listarDetalles(idPedido: Int): List<DetallePedido> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<DetallePedido>()
        val query = """
            SELECT d.${DBHelper.DET_ID}, d.${DBHelper.DET_ID_PEDIDO}, d.${DBHelper.DET_ID_PLATO}, 
                   d.${DBHelper.DET_CANTIDAD}, d.${DBHelper.DET_PRECIO_UNIT}, d.${DBHelper.DET_SUBTOTAL}, 
                   p.${DBHelper.P_NOMBRE} AS nombre_plato
            FROM ${DBHelper.TABLA_DETALLE} d
            INNER JOIN ${DBHelper.TABLA_PLATO} p ON d.${DBHelper.DET_ID_PLATO} = p.${DBHelper.P_ID}
            WHERE d.${DBHelper.DET_ID_PEDIDO} = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(idPedido.toString()))
        while (cursor.moveToNext()) {
            lista.add(
                DetallePedido(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DET_ID)),
                    idPedido = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DET_ID_PEDIDO)),
                    idPlato = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DET_ID_PLATO)),
                    cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DET_CANTIDAD)),
                    precioUnit = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.DET_PRECIO_UNIT)),
                    subtotal = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.DET_SUBTOTAL)),
                    nombrePlato = cursor.getString(cursor.getColumnIndexOrThrow("nombre_plato"))
                )
            )
        }
        cursor.close()
        return lista
    }

    fun eliminarDetalle(idDetalle: Int, idPedido: Int): Boolean {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        return try {
            db.delete(DBHelper.TABLA_DETALLE, "${DBHelper.DET_ID} = ?", arrayOf(idDetalle.toString()))
            recalcularTotalInt(db, idPedido)
            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
        }
    }

    fun cerrarCuenta(idPedido: Int, idMesa: Int): Boolean {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        return try {
            val total = recalcularTotalInt(db, idPedido)

            // Actualizar pedido a CERRADO con total final
            val pedValues = ContentValues().apply {
                put(DBHelper.PED_ESTADO, "CERRADO")
                put(DBHelper.PED_TOTAL, total)
            }
            db.update(DBHelper.TABLA_PEDIDO, pedValues, "${DBHelper.PED_ID} = ?", arrayOf(idPedido.toString()))

            // Actualizar mesa a LIBRE
            val mesaValues = ContentValues().apply {
                put(DBHelper.M_ESTADO, "LIBRE")
            }
            db.update(DBHelper.TABLA_MESA, mesaValues, "${DBHelper.M_ID} = ?", arrayOf(idMesa.toString()))

            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
        }
    }

    private fun recalcularTotalInt(db: SQLiteDatabase, idPedido: Int): Double {
        val query = "SELECT SUM(${DBHelper.DET_SUBTOTAL}) FROM ${DBHelper.TABLA_DETALLE} WHERE ${DBHelper.DET_ID_PEDIDO} = ?"
        val cursor = db.rawQuery(query, arrayOf(idPedido.toString()))
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()

        val values = ContentValues().apply { put(DBHelper.PED_TOTAL, total) }
        db.update(DBHelper.TABLA_PEDIDO, values, "${DBHelper.PED_ID} = ?", arrayOf(idPedido.toString()))
        return total
    }
}
