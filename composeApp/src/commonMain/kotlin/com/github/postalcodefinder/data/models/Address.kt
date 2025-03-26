package com.github.postalcodefinder.data.models

data class Address(
    var postalCode: String = "",
    var street: String = "",
    var neighborhood: String = "",
    var city: String = "",
    var state: String = "",
)
