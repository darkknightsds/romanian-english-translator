package com.darkknightsds.romanianenglishtranslator.presenter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.darkknightsds.romanianenglishtranslator.R
import com.darkknightsds.romanianenglishtranslator.repository.TranslationRepository
import com.darkknightsds.romanianenglishtranslator.model.Translation
import com.google.gson.Gson
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import com.google.gson.reflect.TypeToken

//Primary business logic class for views
class TranslationPresenter(private val context: Context): KoinComponent {
    //Values
    private val translationRepository: TranslationRepository by inject()
    //Variables
    private lateinit var callback:(recentTranslations: String) -> Unit

    //Get translation from Yandex API
    fun getResults(textToTranslate: String, languageConfig: String, options: String, callback: (translatedText: String) -> Unit) {
        translationRepository.getResults(textToTranslate, languageConfig, options, callback)
    }

    //Check to see if user's device has network access
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    //Process most recent translation for use in RecentTranslationsFragment
    fun processRecentTranslations(translation: Translation, callback: (recentTranslations: String) -> Unit) {
        this.callback = callback

        if (!checkForStringInSharedPreferences(context.resources.getString(R.string.recent_translations))) {
            val retrievedArrayList = convertJsonStringToArrayList(translationRepository.getStringFromSharedPreferences(context.resources.getString(
                R.string.recent_translations
            )))
                if (retrievedArrayList.size < 5) {
                    retrievedArrayList.add(translation)
                    saveRecentTranslationsArrayListToSharedPreferences(retrievedArrayList)
                } else {
                    val oversizedArray = retrievedArrayList.toMutableList().apply {
                        removeAt(0)
                    }
                    oversizedArray.add(translation)
                    saveRecentTranslationsArrayListToSharedPreferences(ArrayList(oversizedArray))
                }
        } else {
            val translationsArrayList = ArrayList<Translation>()
            translationsArrayList.add(translation)
            saveRecentTranslationsArrayListToSharedPreferences(translationsArrayList)
        }
    }

    fun convertJsonStringToArrayList(json: String): ArrayList<Translation> {
        return Gson().fromJson(json, object : TypeToken<ArrayList<Translation>>() {}.type)
    }

    fun checkForStringInSharedPreferences(key: String): Boolean {
        return translationRepository.checkForStringInSharedPreferences(key)
    }

    fun getStringFromSharedPreferences(key: String): String {
        return translationRepository.getStringFromSharedPreferences(key)
    }

    fun deleteStringFromSharedPreferences(key: String) {
        translationRepository.deleteStringFromSharedPreferences(key)
    }

    private fun saveRecentTranslationsArrayListToSharedPreferences(arrayList: ArrayList<Translation>) {
        val json = Gson().toJson(arrayList)
        translationRepository.saveStringToSharedPreferences(
            context.resources.getString(R.string.recent_translations),
            json
        )
        callback(json)
    }
}