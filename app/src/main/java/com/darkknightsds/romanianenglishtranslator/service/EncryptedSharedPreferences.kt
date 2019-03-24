package com.darkknightsds.romanianenglishtranslator.service

import android.content.Context
import android.util.Base64
import com.darkknightsds.romanianenglishtranslator.R
import java.nio.charset.Charset


class EncryptedSharedPreferences(val context: Context) {
    //Values
    val prefs = context.getSharedPreferences(context.resources.getString(R.string.shared_preferences), Context.MODE_PRIVATE)
    val editor = context.getSharedPreferences(context.resources.getString(R.string.shared_preferences), Context.MODE_PRIVATE).edit()

    fun saveStringToSharedPreferences(key: String, value: String) {
        val base64 = Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
        editor.putString(key, base64)
        editor.apply()
    }

    fun getStringFromSharedPreferences(key: String): String {
        val encrypted = Base64.decode(prefs.getString(key, null), Base64.DEFAULT)
        return String(encrypted, Charset.defaultCharset())
    }

    fun checkForStringInSharedPreferences(key: String): Boolean {
        return prefs.getString(key, null).isNullOrEmpty()
    }

    fun deleteStringFromSharedPreferences(key: String) {
        editor.remove(key)
        editor.apply()
    }
}