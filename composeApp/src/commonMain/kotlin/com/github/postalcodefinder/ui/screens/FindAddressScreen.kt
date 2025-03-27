package com.github.postalcodefinder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.postalcodefinder.data.models.Address
import com.github.postalcodefinder.data.services.FindPostalCodeService
import com.github.postalcodefinder.ui.components.AddressRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FindAddressScreen(
    modifier: Modifier = Modifier,
    findPostalCodeService: FindPostalCodeService = remember { FindPostalCodeService() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    var postalCode by remember { mutableStateOf("") }
    var address by remember { mutableStateOf(Address()) }
    var isLoadingAddress by remember { mutableStateOf(false) }
    var hasError = false
    var errorMessage = ""

    Scaffold { innerPadding ->
        Column(
            modifier = modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = postalCode,
                onValueChange = { postalCode = it },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                enabled = postalCode.isNotEmpty(),
                onClick = {
                    isLoadingAddress = true
                    hasError = false
                    errorMessage = ""

                    coroutineScope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                address = findPostalCodeService.execute(postalCode)
                            }
                        } catch (e: Exception) {
                            hasError = true
                            errorMessage = e.toString()
                        } finally {
                            isLoadingAddress = false
                        }
                    }

                }, modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoadingAddress) {
                    CircularProgressIndicator()
                    return@Button
                }
                Text("Buscar")
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                AddressRow(label = "CEP:", value = address.postalCode)
                AddressRow(label = "Logradouro:", value = address.street)
                AddressRow(label = "Bairro:", value = address.neighborhood)
                AddressRow(label = "Localidade:", value = address.city)
                AddressRow(label = "UF:", value = address.state)
            }
        }
    }
}