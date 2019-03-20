package com.darkknightsds.romanianenglishtranslator

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TranslationPresenter(private val context: Context): KoinComponent {
    private val translationService: TranslationService by inject()

    fun getResults(textToTranslate: String, languageConfig: String, options: String, callback: (translatedText: String) -> Unit) {
        translationService.getResults(textToTranslate, languageConfig, options, callback)
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

}