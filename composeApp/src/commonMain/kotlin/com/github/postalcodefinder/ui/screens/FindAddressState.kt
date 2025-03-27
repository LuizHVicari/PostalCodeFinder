package com.github.postalcodefinder.ui.screens

import com.github.postalcodefinder.data.models.Address

data class FindAddressState(
    var postalCode: String = "",
    var address: Address = Address(),
    var isLoadingAddress: Boolean = false,
    var hasError: Boolean = false,
    var errorMessage: String = "",
    )
