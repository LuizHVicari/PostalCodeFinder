package com.github.postalcodefinder.di

import com.github.postalcodefinder.data.services.FindPostalCodeService
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module
import com.github.postalcodefinder.ui.screens.FindAddressViewModel
import org.koin.core.module.Module

val appModule: Module = module {
    single { FindPostalCodeService() }
    viewModelOf<FindAddressViewModel, FindPostalCodeService>(::FindAddressViewModel)
}