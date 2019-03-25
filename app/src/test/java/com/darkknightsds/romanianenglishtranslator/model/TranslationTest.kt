package com.darkknightsds.romanianenglishtranslator.model

import org.junit.Assert.assertTrue
import org.junit.Test

class TranslationTest {
    val en = "cat"
    val ro = "pisica"

    @Test
    fun createTranslation() {
        val translation = Translation(en, ro)
        assertTrue(translation.en == "cat" && translation.ro == "pisica")
    }
}