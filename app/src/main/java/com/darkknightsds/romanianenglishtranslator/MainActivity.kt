package com.darkknightsds.romanianenglishtranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var textToTranslate: String
    private lateinit var options: String
    private val translationService: TranslationService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boldFont = ResourcesCompat.getFont(applicationContext, R.font.pt_sans_bold)
        val regFont = ResourcesCompat.getFont(applicationContext, R.font.pt_sans_reg)

        textView_enLabel.text = resources.getString(R.string.english_label)
        textView_enLabel.typeface = boldFont
        textView_roLabel.text = resources.getString(R.string.romanian_label)
        textView_roLabel.typeface = boldFont
        textView_yandex.text = resources.getString(R.string.yandex_details)
        textView_yandex.typeface = regFont

        imageView_ro.setOnClickListener(this)
        imageView_en.setOnClickListener(this)

        options = resources.getString(R.string.search_options) + resources.getString(R.string.more_details) + resources.getString(R.string.search_details)

        editText_en.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    englishToRomanian()
                    true
                }
                else -> false
            }
        }
        editText_en.typeface = regFont

        editText_ro.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    romanianToEnglish()
                    true
                }
                else -> false
            }
        }
        editText_ro.typeface = regFont

        val toolbar = findViewById<Toolbar>(R.id.app_toolbar)
        setSupportActionBar(toolbar)
        toolbar.changeToolbarFont()
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
    }

    override fun onClick(v: View) {
        if (v == imageView_ro) {
            englishToRomanian()
        }
        if (v == imageView_en) {
            romanianToEnglish()
        }
    }

    private fun englishToRomanian() {
        textToTranslate = editText_en.text.trim().toString()
        if (textToTranslate.isEmpty()) {
            editText_en.error = resources.getString(R.string.error_en)
        } else {
            val inputMethodManager = getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus.windowToken, 0
            )
            val languageConfig = resources.getString(R.string.eng_ro)
            translationService.getResults(textToTranslate, languageConfig, options, callback = this::translationCompleted)
        }
    }

    private fun romanianToEnglish() {
        textToTranslate = editText_ro.text.trim().toString()
        if (textToTranslate.isEmpty()) {
            editText_ro.error = resources.getString(R.string.error_ro)
        } else {
            val inputMethodManager = getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus.windowToken, 0
            )
            val languageConfig = resources.getString(R.string.ro_eng)
            translationService.getResults(textToTranslate, languageConfig, options, callback = this::translationCompleted
            )
        }
    }


    private fun translationCompleted(translatedText: String, languageConfig: String) {
        runOnUiThread {
            if (languageConfig == resources.getString(R.string.eng_ro)) {
                editText_ro.setText(translatedText)
            }
            if (languageConfig == resources.getString(R.string.ro_eng)) {
                editText_en.setText(translatedText)
            }
        }
    }

    private fun Toolbar.changeToolbarFont(){
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is TextView && view.text == title) {
                view.typeface = ResourcesCompat.getFont(context, R.font.pt_sans_bold)
                break
            }
        }
    }
}
