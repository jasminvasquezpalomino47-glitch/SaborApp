package com.vasquez.saborapp.data

import android.content.ContentValues
import com.vasquez.saborapp.model.Plato

class PlatoDao(private val dbHelper: DBHelper) {

    fun insertar(plato: Plato): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.P_NOMBRE, plato.nombre)
            put(DBHelper.P_CATEGORIA, plato.categoria)
            put(DBHelper.P_PRECIO, plato.precio)
            put(DBHelper.P_DISPONIBLE, if (plato.disponible) 1 else 0)
        }
        return db.insert(DBHelper.TABLA_PLATO, null, values)
    }

    fun listar(): List<Plato> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Plato>()
        val orderBy = "${DBHelper.P_CATEGORIA} ASC, ${DBHelper.P_NOMBRE} ASC"

        val cursor = db.query(
            DBHelper.TABLA_PLATO,
            null,
            null,
            null,
            null,
            null,
            orderBy
        )

        while (cursor.moveToNext()) {
            lista.add(
                Plato(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.P_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.P_NOMBRE)),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.P_CATEGORIA)),
                    precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.P_PRECIO)),
                    disponible = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.P_DISPONIBLE)) == 1
                )
            )
        }
        cursor.close()
        return lista
    }
}
