package com.arturbogea.buscarcep.model

import com.google.gson.annotations.SerializedName

data class Address(
    val cep: String,
    @SerializedName("logradouro")
    val rua: String,
    val bairro: String,
    @SerializedName("localidade")
    val cidade: String,
    @SerializedName("uf")
    val estado: String
)