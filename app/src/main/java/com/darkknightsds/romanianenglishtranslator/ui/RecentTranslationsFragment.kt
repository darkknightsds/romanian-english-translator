package com.darkknightsds.romanianenglishtranslator.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.darkknightsds.romanianenglishtranslator.R
import com.darkknightsds.romanianenglishtranslator.TranslationPresenter
import com.darkknightsds.romanianenglishtranslator.TranslationViewModel
import kotlinx.android.synthetic.main.fragment_recent_translations.*
import org.koin.android.ext.android.inject

class RecentTranslationsFragment : Fragment() {
    //Values
    private val translationPresenter: TranslationPresenter by inject()
    //Variables
    private lateinit var viewModel: TranslationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recent_translations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(TranslationViewModel::class.java)
        } ?: throw Exception()
        viewModel.recentTranslations.observe(this, Observer<String> { item ->
            textView_recentTranslations.text = item
        })
    }
}
