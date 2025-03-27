package com.github.postalcodefinder

import android.app.Application
import com.github.postalcodefinder.di.appModule
import com.github.postalcodefinder.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}