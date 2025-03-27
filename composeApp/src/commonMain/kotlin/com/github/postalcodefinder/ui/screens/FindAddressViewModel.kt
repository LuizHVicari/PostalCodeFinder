package com.github.postalcodefinder.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.postalcodefinder.data.services.FindPostalCodeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindAddressViewModel(
    private val findPostalCodeService: FindPostalCodeService = FindPostalCodeService()
): ViewModel(
) {
    var state by mutableStateOf(FindAddressState())

    fun onChangePostalCode(target: String) {
        state = state.copy(postalCode = target)
    }

    fun onSearchPostalCode() {
        state = state.copy(
            isLoadingAddress = false,
            hasError = false,
            errorMessage = ""
        )
        apiSearch()


    }

    private fun apiSearch() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val address = findPostalCodeService.execute(state.postalCode)
                    state = state.copy(
                        address = address
                    )
                }
            } catch (ex: Exception) {
                state  = state.copy(
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