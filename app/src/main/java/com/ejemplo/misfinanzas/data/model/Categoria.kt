package com.ejemplo.misfinanzas.data.model

// Cada categoría tiene un emoji y una etiqueta legible
enum class Categoria(
    val emoji: String,
    val etiqueta: String
) {
    COMIDA("🍔", "Comida"),
    TRANSPORTE("🚗", "Transporte"),
    SALARIO("💰", "Salario"),
    ENTRETENIMIENTO("🎮", "Entretenimiento"),
    SERVICIOS("🏠", "Servicios"),
    SALUD("🏥", "Salud"),
    EDUCACION("📚", "Educación"),
    FREELANCE("💻", "Freelance"),
    OTROS("📦", "Otros");

    // Retorna true si esta categoría representa un ingreso
    fun esIngreso(): Boolean {
        return this == SALARIO || this == FREELANCE
    }
}