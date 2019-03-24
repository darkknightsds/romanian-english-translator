package com.darkknightsds.romanianenglishtranslator

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class TranslationApplication : Application() {
    private val appModule = module {
        single { TranslationPresenter(applicationContext) }
        single { TranslationService() }
        single { TranslationFragment() }
        single { EncryptedSharedPreferences(applicationContext) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}
