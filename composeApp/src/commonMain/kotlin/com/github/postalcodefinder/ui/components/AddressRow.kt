package com.github.postalcodefinder.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddressRow(modifier: Modifier = Modifier, label: String, value: String) {
    Row {
        Text(label, style = MaterialTheme.typography.h5)
        Text(value, style = MaterialTheme.typography.body1)
    }
}