package com.github.postalcodefinder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.postalcodefinder.ui.components.AddressRow
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FindAddressScreen(
    modifier: Modifier = Modifier,
    viewModel: FindAddressViewModel = koinViewModel<FindAddressViewModel>()
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(viewModel.state.hasError) {
        if (viewModel.state.hasError) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message = viewModel.state.errorMessage.ifEmpty {
                    "Erro desconhecido"
                })
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState, modifier = modifier.fillMaxSize(),
        snackbarHost = { hostState ->
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier.padding(16.dp),
                snackbar = { snackbarData ->
                    Snackbar(
                        modifier = Modifier.padding(bottom = 80.dp)
                    ) {
                        Text(snackbarData.message)
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.state.postalCode,
                onValueChange = viewModel::onChangePostalCode,
                modifier = Modifier.fillMaxWidth(),
                isError = (!viewModel.isValidCep(viewModel.state.postalCode) && viewModel.state.postalCode.isNotEmpty()) || viewModel.state.hasError,
                enabled = !viewModel.state.isLoadingAddress
            )
            Button(
                enabled = viewModel.state.postalCode.isNotEmpty() && viewModel.isValidCep(viewModel.state.postalCode) && !viewModel.state.isLoadingAddress,
                onClick = viewModel::onSearchPostalCode, modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.state.isLoadingAddress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = androidx.compose.material.MaterialTheme.colors.onPrimary
                    )
                } else {
                    Text("Buscar")
                }
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