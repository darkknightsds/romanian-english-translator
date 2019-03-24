package com.darkknightsds.romanianenglishtranslator

import com.darkknightsds.romanianenglishtranslator.service.EncryptedSharedPreferences
import com.darkknightsds.romanianenglishtranslator.service.TranslationService
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class TranslationRepository: KoinComponent {
    private val translationService: TranslationService by inject()
    private val encryptedSharedPreferences: EncryptedSharedPreferences by inject()

    fun getResults(textToTranslate: String, languageConfig: String, options: String, callback: (translatedText: String) -> Unit) {
        translationService.getResults(textToTranslate, languageConfig, options, callback)
    }

    fun saveStringToSharedPreferences(key: String, value: String) {
        encryptedSharedPreferences.saveStringToSharedPreferences(key, value)
    }

    fun getStringFromSharedPreferences(key: String): String {
        return encryptedSharedPreferences.getStringFromSharedPreferences(key)
    }

    fun checkForStringInSharedPreferences(key: String): Boolean {
        return encryptedSharedPreferences.checkForStringInSharedPreferences(key)
    }

    fun deleteStringFromSharedPreferences(key: String) {
        encryptedSharedPreferences.deleteStringFromSharedPreferences(key)
    }
}