// TasaCambioResponse.kt
// Data class que representa la respuesta JSON de la API de tasas de cambio
package com.ejemplo.misfinanzas.api

// Gson convierte automáticamente los campos del JSON a propiedades de la clase
// Los nombres de las propiedades deben coincidir con las claves del JSON
// Si no coinciden, se usa @SerializedName para mapearlos
data class TasaCambioResponse(
    val result: String,
    val provider: String,
    val base_code: String,
    val rates: Map<String, Double>
)
