package com.ejemplo.misfinanzas.data.remote

import com.ejemplo.misfinanzas.data.model.TasaCambioResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("latest/{base}")
    suspend fun obtenerTasas(
        @Path("base") monedaBase: String = "USD"
    ): TasaCambioResponse
}