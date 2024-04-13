package com.arturbogea.buscarcep.api

import com.arturbogea.buscarcep.model.Address
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AndressAPI {

    @GET("ws/{cep}/json/")

    suspend fun recuperarEndereco(@Path("cep") cepDigitado: String) : Response<Address>

}