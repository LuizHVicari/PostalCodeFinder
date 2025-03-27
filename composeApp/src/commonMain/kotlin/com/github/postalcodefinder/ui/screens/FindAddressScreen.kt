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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FindAddressScreen(
    modifier: Modifier = Modifier,
    viewModel: FindAddressViewModel = koinViewModel<FindAddressViewModel>()
) {

    Scaffold { innerPadding ->
        Column(
            modifier = modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewModel.state.postalCode,
                onValueChange = viewModel::onChangePostalCode,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                enabled = viewModel.state.postalCode.isNotEmpty(),
                onClick = viewModel::onSearchPostalCode, modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.state.isLoadingAddress) {
                    CircularProgressIndicator()
                    return@Button
                }
                Text("Buscar")
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                AddressRow(label = "CEP:", value = viewModel.state.address.postalCode)
                AddressRow(label = "Logradouro:", value = viewModel.state.address.street)
                AddressRow(label = "Bairro:", value = viewModel.state.address.neighborhood)
                AddressRow(label = "Localidade:", value = viewModel.state.address.city)
                AddressRow(label = "UF:", value = viewModel.state.address.state)
            }
        }
    }
}