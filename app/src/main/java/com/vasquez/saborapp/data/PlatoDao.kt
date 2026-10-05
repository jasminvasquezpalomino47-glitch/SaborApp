package com.vasquez.saborapp.data

import android.content.ContentValues
import android.database.Cursor
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

    fun actualizar(plato: Plato): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.P_NOMBRE, plato.nombre)
            put(DBHelper.P_CATEGORIA, plato.categoria)
            put(DBHelper.P_PRECIO, plato.precio)
            put(DBHelper.P_DISPONIBLE, if (plato.disponible) 1 else 0)
        }
        return db.update(
            DBHelper.TABLA_PLATO,
            values,
            "${DBHelper.P_ID} = ?",
            arrayOf(plato.id.toString())
        )
    }

    fun eliminar(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val rows = db.delete(
            DBHelper.TABLA_PLATO,
            "${DBHelper.P_ID} = ?",
            arrayOf(id.toString())
        )
        return rows > 0
    }

    fun obtenerPorId(id: Int): Plato? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLA_PLATO,
            null,
            "${DBHelper.P_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        var plato: Plato? = null
        if (cursor.moveToFirst()) {
            plato = cursorToPlato(cursor)
        }
        cursor.close()
        return plato
    }

    fun listar(filtro: String? = null): List<Plato> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Plato>()

        val selection = if (!filtro.isNullOrBlank()) "${DBHelper.P_NOMBRE} LIKE ?" else null
        val selectionArgs = if (!filtro.isNullOrBlank()) arrayOf("%$filtro%") else null
        val orderBy = "${DBHelper.P_CATEGORIA} ASC, ${DBHelper.P_NOMBRE} ASC"

        val cursor = db.query(
            DBHelper.TABLA_PLATO,
            null,
            selection,
            selectionArgs,
            null,
            null,
            orderBy
        )

        while (cursor.moveToNext()) {
            lista.add(cursorToPlato(cursor))
        }
        cursor.close()

        // Si la tabla está vacía (sin filtro), insertar platos por defecto
        if (lista.isEmpty() && filtro.isNullOrBlank()) {
            insertar(Plato(nombre = "1/2 Pollo a la Brasa", categoria = "Fondos", precio = 35.0, disponible = true))
            insertar(Plato(nombre = "Pollo Entero + Papas", categoria = "Fondos", precio = 65.0, disponible = true))
            insertar(Plato(nombre = "Tequeños de Queso", categoria = "Entradas", precio = 15.0, disponible = true))
            insertar(Plato(nombre = "Inka Kola 1.5L", categoria = "Bebidas", precio = 10.0, disponible = true))
            insertar(Plato(nombre = "Chicha Morada Jarra", categoria = "Bebidas", precio = 12.0, disponible = true))
            return listar(filtro)
        }

        return lista
    }

    fun listarDisponibles(): List<Plato> {
        val list = listar().filter { it.disponible }
        return list
    }

    private fun cursorToPlato(cursor: Cursor): Plato {
        return Plato(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.P_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.P_NOMBRE)),
            categoria = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.P_CATEGORIA)),
            precio = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.P_PRECIO)),
            disponible = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.P_DISPONIBLE)) == 1
        )
    }
}
