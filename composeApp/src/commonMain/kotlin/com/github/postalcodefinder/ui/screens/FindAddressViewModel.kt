package com.github.postalcodefinder.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.postalcodefinder.data.models.Address
import com.github.postalcodefinder.data.services.FindPostalCodeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindAddressViewModel(
    private val findPostalCodeService: FindPostalCodeService = FindPostalCodeService()
) : ViewModel(
) {
    var state by mutableStateOf(FindAddressState())

    fun onChangePostalCode(target: String) {
        state = state.copy(postalCode = target)
    }

    fun onSearchPostalCode() {
        val postalCode = state.postalCode
        setLoadingState()
        apiSearch(postalCode)
    }

    fun isValidCep(cep: String): Boolean {
        val digitsOnly = cep.replace(Regex("[^0-9]"), "")
        return digitsOnly.length == 8
    }

    private fun setLoadingState() {
        state = state.copy(
            isLoadingAddress = true,
            hasError = false,
            errorMessage = "",
            address = Address(),
            postalCode = ""
        )
    }

    private fun apiSearch(postalCode: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val address = findPostalCodeService.execute(postalCode)
                    state = state.copy(
                        address = address
                    )
                }
            } catch (ex: Exception) {
                state = state.copy(
                    hasError = true,
                    errorMessage = ex.toString()
                )
            } finally {
                state = state.copy(
                    isLoadingAddress = false
                )
            }
        }
    }
}