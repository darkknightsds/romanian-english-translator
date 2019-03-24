package com.darkknightsds.romanianenglishtranslator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TranslationViewModel: ViewModel() {
    val recentTranslations = MutableLiveData<String>()

    fun newTranslationAdded(recentTranslations: String) {
        this.recentTranslations.value = recentTranslations
    }
}