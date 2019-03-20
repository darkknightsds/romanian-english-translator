package com.darkknightsds.romanianenglishtranslator

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import android.view.inputmethod.EditorInfo
import android.text.InputType
import android.speech.SpeechRecognizer
import android.speech.RecognizerIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val translationPresenter: TranslationPresenter by inject()
    private val REQUEST_RECORD_PERMISSION = 1007

    private lateinit var textToTranslate: String
    private lateinit var options: String
    private lateinit var languageConfig: String
    private lateinit var translateEditText: EditText
    private lateinit var resultsEditText: EditText
    private lateinit var speechRecognizer: SpeechRecognizer

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
        button_enToRo.text = resources.getString(R.string.english_translate)
        button_enToRo.typeface = boldFont
        button_roToEn.text = resources.getString(R.string.romanian_translate)
        button_roToEn.typeface = boldFont

        button_enToRo.setOnClickListener(this)
        button_roToEn.setOnClickListener(this)
        button_speechEn.setOnClickListener(this)
        button_speechRo.setOnClickListener(this)

        options = resources.getString(R.string.search_options) + resources.getString(R.string.more_details) + resources.getString(R.string.search_details)

        editText_translateEn.typeface = regFont
        editText_translateEn.inputType = InputType.TYPE_CLASS_TEXT
        editText_translateEn.setSingleLine(true)
        editText_translateEn.setLines(6)
        editText_translateEn.setHorizontallyScrolling(false)
        editText_translateEn.imeOptions = EditorInfo.IME_ACTION_DONE

        editText_translateRo.typeface = regFont
        editText_translateRo.inputType = InputType.TYPE_CLASS_TEXT
        editText_translateRo.setSingleLine(true)
        editText_translateRo.setLines(6)
        editText_translateRo.setHorizontallyScrolling(false)
        editText_translateRo.imeOptions = EditorInfo.IME_ACTION_DONE

        findViewById<EditText>(R.id.editText_translateEn).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    languageConfig = resources.getString(R.string.eng_ro)
                    translateEditText = editText_translateEn
                    resultsEditText = editText_translateRo
                    translate()
                    true
                }
                else -> false
            }
        }

        findViewById<EditText>(R.id.editText_translateRo).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    languageConfig = resources.getString(R.string.ro_eng)
                    translateEditText = editText_translateRo
                    resultsEditText = editText_translateEn
                    translate()
                    true
                }
                else -> false
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.app_toolbar)
        setSupportActionBar(toolbar)
        toolbar.changeToolbarFont()
        toolbar.setTitleTextColor(resources.getColor(R.color.white))

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(SpeechListener(callback = this::displaySpeechText))
    }

    override fun onClick(v: View) {
        when (v) {
            button_enToRo -> {
                languageConfig = resources.getString(R.string.eng_ro)
                translateEditText = editText_translateEn
                resultsEditText = editText_translateRo
                translate()
            }
            button_roToEn -> {
                languageConfig = resources.getString(R.string.ro_eng)
                translateEditText = editText_translateRo
                resultsEditText = editText_translateEn
                translate()
            }
            button_speechEn -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO
                        ), REQUEST_RECORD_PERMISSION
                    )
                }
                languageConfig = resources.getString(R.string.eng_ro)
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, javaClass.getPackage()!!.name)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
                speechRecognizer.startListening(intent)
            }
            button_speechRo -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO
                        ), REQUEST_RECORD_PERMISSION
                    )
                }
                languageConfig = resources.getString(R.string.ro_eng)
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, javaClass.getPackage()!!.name)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ro")
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
                speechRecognizer.startListening(intent)
            }
        }
    }

    private fun translate() {
        if (!translationPresenter.isNetworkAvailable()) {
            Toast.makeText(this, resources.getString(R.string.error_no_net), Toast.LENGTH_SHORT).show()
        } else {
            textToTranslate = translateEditText.text.trim().toString()
            if (textToTranslate.isEmpty()) {
                if (languageConfig == resources.getString(R.string.eng_ro)) {
                    translateEditText.error = resources.getString(R.string.error_en)
                } else {
                    translateEditText.error = resources.getString(R.string.error_ro)
                }
            } else {
                val inputMethodManager = getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    currentFocus?.windowToken, 0
                )
                translationPresenter.getResults(textToTranslate, languageConfig, options, callback = this::translationCompleted)
                progressBar_translate.visibility = View.VISIBLE
                button_enToRo.isEnabled = false
                button_roToEn.isEnabled = false
                button_speechEn.isEnabled = false
                button_speechRo.isEnabled = false
            }
        }
    }

    private fun translationCompleted(translatedText: String) {
        runOnUiThread {
            progressBar_translate.visibility = View.GONE
            resultsEditText.setText(translatedText)
            button_enToRo.isEnabled = true
            button_roToEn.isEnabled = true
            button_speechEn.isEnabled = true
            button_speechRo.isEnabled = true
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

    private fun displaySpeechText(results: String) {
        runOnUiThread {
            if (languageConfig == resources.getString(R.string.eng_ro)) {
                editText_translateEn.append(" $results")
            } else {
                editText_translateRo.append(" $results")
            }
        }
    }

    override fun finish() {
        super.finish()
        speechRecognizer.destroy()
    }
}
