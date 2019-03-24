package com.darkknightsds.romanianenglishtranslator

import android.speech.RecognitionListener
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log

class SpeechListener(val callback: (results: String) -> Unit): RecognitionListener {
    //Values
    private val TAG = javaClass.simpleName

    override fun onReadyForSpeech(params: Bundle) {
        Log.d(TAG, "onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.d(TAG, "onRmsChanged")
    }

    override fun onBufferReceived(buffer: ByteArray) {
        Log.d(TAG, "onBufferReceived")
    }

    override fun onEndOfSpeech() {
        Log.d(TAG, "onEndofSpeech")
    }

    override fun onError(error: Int) {
        Log.d(TAG, "Error: $error")
    }

    override fun onResults(results: Bundle) {
        Log.d(TAG, "onResults $results")
        val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        callback(matches[0])
    }

    override fun onPartialResults(partialResults: Bundle) {
        Log.d(TAG, "onPartialResults")
    }

    override fun onEvent(eventType: Int, params: Bundle) {
        Log.d(TAG, "onEvent $eventType")
    }
}