package com.darkknightsds.romanianenglishtranslator.service

import org.junit.Test

import org.junit.Assert.*
import org.koin.standalone.inject
import org.koin.test.KoinTest

class EncryptedSharedPreferencesTest: KoinTest {
    private val testData = "cat"
    private val encryptedSharedPreferences: EncryptedSharedPreferences by inject()

    @Test
    fun saveAndGetStringFromSharedPreferences() {
        encryptedSharedPreferences.saveStringToSharedPreferences("data", testData)
        val data = (encryptedSharedPreferences.getStringFromSharedPreferences("data"))
        assertTrue(data == testData)
    }

    @Test
    fun checkForStringInSharedPreferences() {
        encryptedSharedPreferences.saveStringToSharedPreferences("data", testData)
        assertTrue(!encryptedSharedPreferences.checkForStringInSharedPreferences("data"))
    }

    @Test
    fun deleteStringFromSharedPreferences() {
        encryptedSharedPreferences.saveStringToSharedPreferences("data", testData)
        encryptedSharedPreferences.deleteStringFromSharedPreferences("data")
        assertTrue((encryptedSharedPreferences.checkForStringInSharedPreferences("data")))
    }
}