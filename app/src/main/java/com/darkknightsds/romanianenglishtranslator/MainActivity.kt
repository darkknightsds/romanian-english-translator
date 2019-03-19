package com.darkknightsds.romanianenglishtranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var textToTranslate: String
    private lateinit var options: String
    private lateinit var languageConfig: String
    private val translationPresenter: TranslationPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        languageConfig = resources.getString(R.string.eng_ro)

        val boldFont = ResourcesCompat.getFont(applicationContext, R.font.pt_sans_bold)
        val regFont = ResourcesCompat.getFont(applicationContext, R.font.pt_sans_reg)

        textView_enLabel.text = resources.getString(R.string.english_label_en)
        textView_enLabel.typeface = boldFont
        textView_roLabel.text = resources.getString(R.string.romanian_label_en)
        textView_roLabel.typeface = boldFont
        textView_yandex.text = resources.getString(R.string.yandex_details)
        textView_yandex.typeface = regFont
        button_translate.text = resources.getString(R.string.english_translate)
        button_translate.typeface = boldFont

        button_translate.setOnClickListener(this)
        imageButton_swap.setOnClickListener(this)

        options = resources.getString(R.string.search_options) + resources.getString(R.string.more_details) + resources.getString(R.string.search_details)

        editText_translate.typeface = regFont

        val toolbar = findViewById<Toolbar>(R.id.app_toolbar)
        setSupportActionBar(toolbar)
        toolbar.changeToolbarFont()
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
    }

    override fun onClick(v: View) {
        when (v) {
            button_translate -> {
                translate()
            }
            imageButton_swap -> {
                if (languageConfig == resources.getString(R.string.eng_ro)) {
                    Log.d("clicked", "eng-ro")
                    languageConfig = resources.getString(R.string.ro_eng)
                    button_translate.text = resources.getString(R.string.romanian_translate)
                    textView_enLabel.text = resources.getString(R.string.english_label_ro)
                    textView_roLabel.text = resources.getString(R.string.romanian_label_ro)
                } else {
                    Log.d("clicked", "ro-eng")
                    languageConfig = resources.getString(R.string.eng_ro)
                    button_translate.text = resources.getString(R.string.english_translate)
                    textView_enLabel.text = resources.getString(R.string.english_label_en)
                    textView_roLabel.text = resources.getString(R.string.romanian_label_en)
                }
            }
        }
    }

    private fun translate() {
        if (!translationPresenter.isNetworkAvailable()) {
            Toast.makeText(this, resources.getString(R.string.error_no_net), Toast.LENGTH_SHORT).show()
        } else {
            textToTranslate = editText_translate.text.trim().toString()
            if (textToTranslate.isEmpty()) {
                editText_translate.error = resources.getString(R.string.error_en)
            } else {
                val inputMethodManager = getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    currentFocus?.windowToken, 0
                )
                translationPresenter.getResults(textToTranslate, languageConfig, options, callback = this::translationCompleted)
            }
        }
    }

    private fun translationCompleted(translatedText: String, languageConfig: String) {
        runOnUiThread {
            editText_translate.setText(translatedText)
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
