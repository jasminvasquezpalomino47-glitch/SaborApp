package com.vasquez.saborapp.data

import android.content.ContentValues
import com.vasquez.saborapp.model.Mesa

class MesaDao(private val dbHelper: DBHelper) {

    fun insertar(mesa: Mesa): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.M_NUMERO, mesa.numero)
            put(DBHelper.M_CAPACIDAD, mesa.capacidad)
            put(DBHelper.M_ESTADO, mesa.estado)
        }
        return db.insert(DBHelper.TABLA_MESA, null, values)
    }

    fun listar(): List<Mesa> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Mesa>()
        val cursor = db.query(
            DBHelper.TABLA_MESA,
            null,
            null,
            null,
            null,
            null,
            "${DBHelper.M_NUMERO} ASC"
        )
        while (cursor.moveToNext()) {
            lista.add(
                Mesa(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.M_ID)),
                    numero = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.M_NUMERO)),
                    capacidad = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.M_CAPACIDAD)),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.M_ESTADO))
                )
            )
        }
        cursor.close()
        return lista
    }
}
