package com.darkknightsds.romanianenglishtranslator.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import com.darkknightsds.romanianenglishtranslator.*
import kotlinx.android.synthetic.main.fragment_translation.*
import org.koin.android.ext.android.inject

class TranslationFragment : Fragment(), View.OnClickListener {
    //Values
    private val translationPresenter: TranslationPresenter by inject()
    private val REQUEST_RECORD_PERMISSION = 1007
    //Variables
    private lateinit var textToTranslate: String
    private lateinit var options: String
    private lateinit var languageConfig: String
    private lateinit var translateEditText: EditText
    private lateinit var resultsEditText: EditText
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var viewModel: TranslationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        languageConfig = resources.getString(R.string.en_ro)

        val boldFont = ResourcesCompat.getFont(activity!!.applicationContext,
            R.font.pt_sans_bold
        )
        val regFont = ResourcesCompat.getFont(activity!!.applicationContext,
            R.font.pt_sans_reg
        )

        textView_enLabel.text = resources.getString(R.string.english_label_en)
        textView_enLabel.typeface = boldFont
        textView_roLabel.text = resources.getString(R.string.romanian_label_en)
        textView_roLabel.typeface = boldFont
        button_enToRo.text = resources.getString(R.string.english_translate)
        button_enToRo.typeface = boldFont
        button_roToEn.text = resources.getString(R.string.romanian_translate)
        button_roToEn.typeface = boldFont

        button_enToRo.setOnClickListener(this)
        button_roToEn.setOnClickListener(this)
        button_speechEn.setOnClickListener(this)
        button_speechRo.setOnClickListener(this)

        options = resources.getString(R.string.search_options) + resources.getString(
            R.string.more_details
        ) + resources.getString(R.string.search_details)

        editText_translateEn.typeface = regFont
        editText_translateEn.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        editText_translateEn.setSingleLine(true)
        editText_translateEn.setLines(6)
        editText_translateEn.setHorizontallyScrolling(false)
        editText_translateEn.imeOptions = EditorInfo.IME_ACTION_DONE

        editText_translateRo.typeface = regFont
        editText_translateRo.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        editText_translateRo.setSingleLine(true)
        editText_translateRo.setLines(6)
        editText_translateRo.setHorizontallyScrolling(false)
        editText_translateRo.imeOptions = EditorInfo.IME_ACTION_DONE

        getView()!!.findViewById<EditText>(R.id.editText_translateEn).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    languageConfig = resources.getString(R.string.en_ro)
                    translateEditText = editText_translateEn
                    resultsEditText = editText_translateRo
                    translate()
                    true
                }
                else -> false
            }
        }
        getView()!!.findViewById<EditText>(R.id.editText_translateRo).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    languageConfig = resources.getString(R.string.ro_en)
                    translateEditText = editText_translateRo
                    resultsEditText = editText_translateEn
                    translate()
                    true
                }
                else -> false
            }
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)
        speechRecognizer.setRecognitionListener(SpeechListener(callback = this::displaySpeechText))

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(TranslationViewModel::class.java)
        } ?: throw Exception()
    }

    override fun onClick(v: View) {
        when (v) {
            button_enToRo -> {
                languageConfig = resources.getString(R.string.en_ro)
                translateEditText = editText_translateEn
                resultsEditText = editText_translateRo
                translate()
            }
            button_roToEn -> {
                languageConfig = resources.getString(R.string.ro_en)
                translateEditText = editText_translateRo
                resultsEditText = editText_translateEn
                translate()
            }
            button_speechEn -> {
                if (ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO
                        ), REQUEST_RECORD_PERMISSION
                    )
                }
                languageConfig = resources.getString(R.string.en_ro)
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, javaClass.getPackage()!!.name)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
                speechRecognizer.startListening(intent)
            }
            button_speechRo -> {
                if (ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO
                        ), REQUEST_RECORD_PERMISSION
                    )
                }
                languageConfig = resources.getString(R.string.ro_en)
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
            Toast.makeText(activity, resources.getString(R.string.error_no_net), Toast.LENGTH_SHORT).show()
        } else {
            textToTranslate = translateEditText.text.trim().toString()
            if (textToTranslate.isEmpty()) {
                if (languageConfig == resources.getString(R.string.en_ro)) {
                    translateEditText.error = resources.getString(R.string.error_en)
                } else {
                    translateEditText.error = resources.getString(R.string.error_ro)
                }
            } else {
                val inputMethodManager = activity!!.getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    activity!!.currentFocus?.windowToken, 0
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
        activity!!.runOnUiThread {
            progressBar_translate.visibility = View.GONE
            resultsEditText.setText(translatedText)
            button_enToRo.isEnabled = true
            button_roToEn.isEnabled = true
            button_speechEn.isEnabled = true
            button_speechRo.isEnabled = true

            val translation = Translation(
                editText_translateEn.text.toString(),
                editText_translateRo.text.toString()
            )
            translationPresenter.processRecentTranslations(translation, callback = this::recentTranslationSaved)
        }
    }

    private fun displaySpeechText(results: String) {
        activity!!.runOnUiThread {
            if (languageConfig == resources.getString(R.string.en_ro)) {
                editText_translateEn.append(" $results")
            } else {
                editText_translateRo.append(" $results")
            }
        }
    }

    private fun recentTranslationSaved(recentTranslations: String) {
        viewModel.newTranslationAdded(recentTranslations)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer.destroy()
    }
}
