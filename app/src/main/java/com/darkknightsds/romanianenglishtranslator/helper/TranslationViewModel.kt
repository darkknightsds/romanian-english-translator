package com.darkknightsds.romanianenglishtranslator.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//Simple ViewModel interface to communicate between TranslationFragment and RecentTranslationsFragment
class TranslationViewModel: ViewModel() {
    val recentTranslations = MutableLiveData<String>()

    fun newTranslationAdded(recentTranslations: String) {
        this.recentTranslations.value = recentTranslations
    }
}