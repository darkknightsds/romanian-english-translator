package com.darkknightsds.romanianenglishtranslator

import android.app.Application
import com.darkknightsds.romanianenglishtranslator.ui.RecentTranslationsFragment
import com.darkknightsds.romanianenglishtranslator.ui.TranslationFragment
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class TranslationApplication : Application() {
    //Values
    private val appModule = module {
        single { TranslationRepository() }
        single { TranslationPresenter(applicationContext) }
        single { TranslationService() }
        single { EncryptedSharedPreferences(applicationContext) }
        single { TranslationFragment() }
        single { RecentTranslationsFragment() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}
