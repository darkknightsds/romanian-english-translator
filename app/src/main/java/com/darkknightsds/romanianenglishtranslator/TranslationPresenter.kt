package com.darkknightsds.romanianenglishtranslator

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.google.gson.Gson
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import com.google.gson.reflect.TypeToken

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

        if (!translationRepository.checkForStringInSharedPreferences(context.resources.getString(R.string.recent_translations))) {
            val retrievedArrayList: ArrayList<Translation> = Gson().fromJson(translationRepository.getStringFromSharedPreferences(context.resources.getString(R.string.recent_translations)), object : TypeToken<ArrayList<Translation>>() {}.type)
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

    private fun saveRecentTranslationsArrayListToSharedPreferences(arrayList: ArrayList<Translation>) {
        val json = Gson().toJson(arrayList)
        translationRepository.saveStringToSharedPreferences(
            context.resources.getString(R.string.recent_translations),
            json
        )
        callback(json)
    }
}