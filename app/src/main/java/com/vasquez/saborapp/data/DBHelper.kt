package com.vasquez.saborapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "saborapp.db"
        private const val DATABASE_VERSION = 2

        // Tabla usuario
        const val TABLA_USUARIO = "usuario"
        const val U_ID = "id"
        const val U_USUARIO = "usuario"
        const val U_CLAVE = "clave"
        const val U_ROL = "rol"

        // Tabla plato
        const val TABLA_PLATO = "plato"
        const val P_ID = "id"
        const val P_NOMBRE = "nombre"
        const val P_CATEGORIA = "categoria"
        const val P_PRECIO = "precio"
        const val P_DISPONIBLE = "disponible"

        // Tabla mesa
        const val TABLA_MESA = "mesa"
        const val M_ID = "id"
        const val M_NUMERO = "numero"
        const val M_CAPACIDAD = "capacidad"
        const val M_ESTADO = "estado"

        // Tabla pedido
        const val TABLA_PEDIDO = "pedido"
        const val PED_ID = "id"
        const val PED_ID_MESA = "id_mesa"
        const val PED_FECHA = "fecha"
        const val PED_ESTADO = "estado"
        const val PED_TOTAL = "total"

        // Tabla detalle_pedido
        const val TABLA_DETALLE = "detalle_pedido"
        const val DET_ID = "id"
        const val DET_ID_PEDIDO = "id_pedido"
        const val DET_ID_PLATO = "id_plato"
        const val DET_CANTIDAD = "cantidad"
        const val DET_PRECIO_UNIT = "precio_unit"
        const val DET_SUBTOTAL = "subtotal"
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTablaUsuario = """
            CREATE TABLE $TABLA_USUARIO (
                $U_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $U_USUARIO TEXT UNIQUE NOT NULL,
                $U_CLAVE TEXT NOT NULL,
                $U_ROL TEXT NOT NULL
            );
        """.trimIndent()

        val createTablaPlato = """
            CREATE TABLE $TABLA_PLATO (
                $P_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $P_NOMBRE TEXT NOT NULL,
                $P_CATEGORIA TEXT,
                $P_PRECIO REAL CHECK($P_PRECIO > 0),
                $P_DISPONIBLE INTEGER DEFAULT 1
            );
        """.trimIndent()

        val createTablaMesa = """
            CREATE TABLE $TABLA_MESA (
                $M_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $M_NUMERO INTEGER UNIQUE NOT NULL,
                $M_CAPACIDAD INTEGER NOT NULL,
                $M_ESTADO TEXT DEFAULT 'LIBRE'
            );
        """.trimIndent()

        val createTablaPedido = """
            CREATE TABLE $TABLA_PEDIDO (
                $PED_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $PED_ID_MESA INTEGER NOT NULL,
                $PED_FECHA TEXT NOT NULL,
                $PED_ESTADO TEXT DEFAULT 'ABIERTO',
                $PED_TOTAL REAL DEFAULT 0.0,
                FOREIGN KEY($PED_ID_MESA) REFERENCES $TABLA_MESA($M_ID)
            );
        """.trimIndent()

        val createTablaDetalle = """
            CREATE TABLE $TABLA_DETALLE (
                $DET_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $DET_ID_PEDIDO INTEGER NOT NULL,
                $DET_ID_PLATO INTEGER NOT NULL,
                $DET_CANTIDAD INTEGER CHECK($DET_CANTIDAD > 0),
                $DET_PRECIO_UNIT REAL NOT NULL,
                $DET_SUBTOTAL REAL NOT NULL,
                FOREIGN KEY($DET_ID_PEDIDO) REFERENCES $TABLA_PEDIDO($PED_ID) ON DELETE CASCADE,
                FOREIGN KEY($DET_ID_PLATO) REFERENCES $TABLA_PLATO($P_ID)
            );
        """.trimIndent()

        db.execSQL(createTablaUsuario)
        db.execSQL(createTablaPlato)
        db.execSQL(createTablaMesa)
        db.execSQL(createTablaPedido)
        db.execSQL(createTablaDetalle)

        // Datos iniciales
        db.execSQL("INSERT OR IGNORE INTO $TABLA_USUARIO ($U_USUARIO, $U_CLAVE, $U_ROL) VALUES ('admin', '1234', 'ADMIN');")
        db.execSQL("INSERT OR IGNORE INTO $TABLA_USUARIO ($U_USUARIO, $U_CLAVE, $U_ROL) VALUES ('mozo', '1234', 'MOZO');")

        db.execSQL("INSERT OR IGNORE INTO $TABLA_PLATO ($P_NOMBRE, $P_CATEGORIA, $P_PRECIO, $P_DISPONIBLE) VALUES ('Pollo Entero + Papas', 'Fondos', 65.0, 1);")
        db.execSQL("INSERT OR IGNORE INTO $TABLA_PLATO ($P_NOMBRE, $P_CATEGORIA, $P_PRECIO, $P_DISPONIBLE) VALUES ('1/2 Pollo a la Brasa', 'Fondos', 35.0, 1);")
        db.execSQL("INSERT OR IGNORE INTO $TABLA_PLATO ($P_NOMBRE, $P_CATEGORIA, $P_PRECIO, $P_DISPONIBLE) VALUES ('Tequeños de Queso', 'Entradas', 15.0, 1);")
        db.execSQL("INSERT OR IGNORE INTO $TABLA_PLATO ($P_NOMBRE, $P_CATEGORIA, $P_PRECIO, $P_DISPONIBLE) VALUES ('Inka Kola 1.5L', 'Bebidas', 10.0, 1);")

        db.execSQL("INSERT OR IGNORE INTO $TABLA_MESA ($M_NUMERO, $M_CAPACIDAD, $M_ESTADO) VALUES (1, 4, 'LIBRE');")
        db.execSQL("INSERT OR IGNORE INTO $TABLA_MESA ($M_NUMERO, $M_CAPACIDAD, $M_ESTADO) VALUES (2, 2, 'LIBRE');")
        db.execSQL("INSERT OR IGNORE INTO $TABLA_MESA ($M_NUMERO, $M_CAPACIDAD, $M_ESTADO) VALUES (3, 6, 'LIBRE');")
        db.execSQL("INSERT OR IGNORE INTO $TABLA_MESA ($M_NUMERO, $M_CAPACIDAD, $M_ESTADO) VALUES (4, 4, 'LIBRE');")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_DETALLE")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PEDIDO")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_MESA")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PLATO")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_USUARIO")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}
