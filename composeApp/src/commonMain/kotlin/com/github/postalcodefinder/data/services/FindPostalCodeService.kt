package com.github.postalcodefinder.data.services


import com.github.postalcodefinder.data.models.Address
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class FindPostalCodeService {
    private val client = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private val baseUrl = "https://viacep.com.br/ws"


    @Serializable
    private data class ViaCEPResponse(
        val cep: String = "",
        val logradouro: String = "",
        val bairro: String = "",
        val localidade: String = "",
        val uf: String = "",
        val erro: Boolean = false
    )

    suspend fun execute(postalCode: String): Address {
        val formattedPostalCode = postalCode.replace(Regex("[^0-9]"), "")

        if (formattedPostalCode.length != 8) {
            throw Exception("CEP inválido")
        }

        val response = client.get("$baseUrl/$formattedPostalCode/json").body<ViaCEPResponse>()

        if (response.erro) {
            throw Exception("Localização não encontrada")
        }

        return Address(
            postalCode = response.cep,
            street = response.logradouro,
            neighborhood = response.bairro,
            city = response.localidade,
            state = response.uf
        )
    }
}