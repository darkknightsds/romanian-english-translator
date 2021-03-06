package com.darkknightsds.romanianenglishtranslator.application

import android.app.Application
import com.darkknightsds.romanianenglishtranslator.presenter.TranslationPresenter
import com.darkknightsds.romanianenglishtranslator.repository.TranslationRepository
import com.darkknightsds.romanianenglishtranslator.service.EncryptedSharedPreferences
import com.darkknightsds.romanianenglishtranslator.service.TranslationService
import com.darkknightsds.romanianenglishtranslator.ui.RecentTranslationsFragment
import com.darkknightsds.romanianenglishtranslator.ui.TranslationFragment
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class TranslationApplication : Application() {
    //Module for dependency injection with Koin
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
