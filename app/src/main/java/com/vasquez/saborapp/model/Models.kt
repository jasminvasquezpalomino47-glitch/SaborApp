package com.vasquez.saborapp.model

data class Usuario(
    val id: Int = 0,
    val usuario: String,
    val clave: String,
    val rol: String
)

data class Plato(
    val id: Int = 0,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val disponible: Boolean = true
)

data class Mesa(
    val id: Int = 0,
    val numero: Int,
    val capacidad: Int,
    val estado: String = "LIBRE"
)

data class Pedido(
    val id: Int = 0,
    val idMesa: Int,
    val fecha: String,
    val estado: String = "ABIERTO",
    val total: Double = 0.0
)

data class DetallePedido(
    val id: Int = 0,
    val idPedido: Int,
    val idPlato: Int,
    val cantidad: Int,
    val precioUnit: Double,
    val subtotal: Double,
    val nombrePlato: String = ""
)

data class ReporteTopPlato(
    val nombrePlato: String,
    val cantidadTotal: Int,
    val ventaTotal: Double
)

data class ReporteVentaMesa(
    val numeroMesa: Int,
    val ventaTotal: Double
)
