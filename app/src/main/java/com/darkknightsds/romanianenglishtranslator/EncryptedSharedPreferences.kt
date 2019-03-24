package com.darkknightsds.romanianenglishtranslator

import android.content.Context
import android.util.Base64

class EncryptedSharedPreferences(val context: Context) {
    val prefs = context.getSharedPreferences(context.resources.getString(R.string.shared_preferences), Context.MODE_PRIVATE)
    val editor = context.getSharedPreferences(context.resources.getString(R.string.shared_preferences), Context.MODE_PRIVATE).edit()

    fun saveStringToSharedPreferences(key: String, value: String) {
        val base64 = Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
        editor.putString(key, base64)
        editor.apply()
    }

    fun getStringFromSharedPreferences(key: String): String {
        return Base64.decode(prefs.getString(key, null), Base64.DEFAULT).toString()
    }

    fun deleteStringFromSharedPrefererences(key: String) {
        editor.remove(key)
        editor.apply()
    }
}