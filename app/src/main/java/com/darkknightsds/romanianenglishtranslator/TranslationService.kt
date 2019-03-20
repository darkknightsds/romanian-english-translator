package com.darkknightsds.romanianenglishtranslator

import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class TranslationService {
    private val client = OkHttpClient()
    private lateinit var resp: String
    private lateinit var respArr: String

    fun getResults(textToTranslate: String, languageConfig: String, options: String, callback: (translatedText: String) -> Unit) {
        val url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=URLINFO&text=TEXTTOTRANSLATE&lang=LANGUAGECONFIG".replace("URLINFO", options) .replace("TEXTTOTRANSLATE", textToTranslate).replace("LANGUAGECONFIG", languageConfig)

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                response.body()!!.use { responseBody ->
                    if (!response.isSuccessful)
                        throw IOException("Unexpected code TranslationService $response")

                    val responseHeaders = response.headers()
                    var i = 0
                    val size = responseHeaders.size()
                    while (i < size) {
                        println(responseHeaders.name(i) + ": " + responseHeaders.value(i))
                        i++
                    }

                    resp = responseBody.string()

                    try {
                        val respObj = JSONObject(resp)
                        respArr = respObj.getJSONArray("text")[0].toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    callback(respArr)
                }
            }
        })
    }
}