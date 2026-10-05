package com.vasquez.saborapp.data

import com.vasquez.saborapp.model.ReporteTopPlato
import com.vasquez.saborapp.model.ReporteVentaMesa

class ReporteDao(private val dbHelper: DBHelper) {

    fun ventaDelDia(): Double {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT SUM(${DBHelper.PED_TOTAL}) 
            FROM ${DBHelper.TABLA_PEDIDO} 
            WHERE ${DBHelper.PED_ESTADO} = 'CERRADO' 
              AND DATE(${DBHelper.PED_FECHA}) = DATE('now', 'localtime')
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        return total
    }

    fun pedidosCerradosHoy(): Int {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT COUNT(*) 
            FROM ${DBHelper.TABLA_PEDIDO} 
            WHERE ${DBHelper.PED_ESTADO} = 'CERRADO' 
              AND DATE(${DBHelper.PED_FECHA}) = DATE('now', 'localtime')
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun topPlatos(): List<ReporteTopPlato> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<ReporteTopPlato>()
        val query = """
            SELECT p.${DBHelper.P_NOMBRE}, 
                   SUM(d.${DBHelper.DET_CANTIDAD}) AS cant_total, 
                   SUM(d.${DBHelper.DET_SUBTOTAL}) AS venta_total
            FROM ${DBHelper.TABLA_DETALLE} d
            INNER JOIN ${DBHelper.TABLA_PLATO} p ON d.${DBHelper.DET_ID_PLATO} = p.${DBHelper.P_ID}
            INNER JOIN ${DBHelper.TABLA_PEDIDO} ped ON d.${DBHelper.DET_ID_PEDIDO} = ped.${DBHelper.PED_ID}
            WHERE ped.${DBHelper.PED_ESTADO} = 'CERRADO'
            GROUP BY p.${DBHelper.P_ID}
            ORDER BY cant_total DESC
            LIMIT 5
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            lista.add(
                ReporteTopPlato(
                    nombrePlato = cursor.getString(0),
                    cantidadTotal = cursor.getInt(1),
                    ventaTotal = cursor.getDouble(2)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun ventaPorMesa(): List<ReporteVentaMesa> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<ReporteVentaMesa>()
        val query = """
            SELECT m.${DBHelper.M_NUMERO}, 
                   SUM(ped.${DBHelper.PED_TOTAL}) AS venta_total
            FROM ${DBHelper.TABLA_PEDIDO} ped
            INNER JOIN ${DBHelper.TABLA_MESA} m ON ped.${DBHelper.PED_ID_MESA} = m.${DBHelper.M_ID}
            WHERE ped.${DBHelper.PED_ESTADO} = 'CERRADO'
            GROUP BY m.${DBHelper.M_ID}
            ORDER BY venta_total DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            lista.add(
                ReporteVentaMesa(
                    numeroMesa = cursor.getInt(0),
                    ventaTotal = cursor.getDouble(1)
                )
            )
        }
        cursor.close()
        return lista
    }
}
