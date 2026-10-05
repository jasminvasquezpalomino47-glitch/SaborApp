package com.vasquez.saborapp.data

import com.vasquez.saborapp.model.Usuario

class UsuarioDao(private val dbHelper: DBHelper) {

    fun validarUsuario(usuario: String, clave: String): Usuario? {
        val db = dbHelper.readableDatabase
        val query = "SELECT ${DBHelper.U_ID}, ${DBHelper.U_USUARIO}, ${DBHelper.U_CLAVE}, ${DBHelper.U_ROL} " +
                "FROM ${DBHelper.TABLA_USUARIO} WHERE ${DBHelper.U_USUARIO} = ? AND ${DBHelper.U_CLAVE} = ?"
        val cursor = db.rawQuery(query, arrayOf(usuario, clave))

        var user: Usuario? = null
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndexOrThrow(DBHelper.U_ID)
            val uIndex = cursor.getColumnIndexOrThrow(DBHelper.U_USUARIO)
            val cIndex = cursor.getColumnIndexOrThrow(DBHelper.U_CLAVE)
            val rIndex = cursor.getColumnIndexOrThrow(DBHelper.U_ROL)

            user = Usuario(
                id = cursor.getInt(idIndex),
                usuario = cursor.getString(uIndex),
                clave = cursor.getString(cIndex),
                rol = cursor.getString(rIndex)
            )
        }
        cursor.close()
        return user
    }
}
