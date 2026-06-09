package com.ejemplo.misfinanzas.data.model

// Gson convierte automáticamente los campos del JSON a propiedades de la clase
// Los nombres de las propiedades deben coincidir con las claves del JSON
// Si no coinciden, se usa @SerializedName para mapearlos
data class TasaCambioResponse(
    val result: String,
    val provider: String,
    val base_code: String,
    val rates: Map<String, Double>
)