package com.darkknightsds.romanianenglishtranslator.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkknightsds.romanianenglishtranslator.*
import kotlinx.android.synthetic.main.fragment_recent_translations.*
import kotlinx.android.synthetic.main.fragment_translation.*
import org.koin.android.ext.android.inject

class RecentTranslationsFragment : Fragment(), View.OnClickListener {
    //Values
    private val translationPresenter: TranslationPresenter by inject()
    //Variables
    private lateinit var viewModel: TranslationViewModel
    private lateinit var recentTranslationsArray: ArrayList<Translation>
    private lateinit var adapter: RecentTranslationsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recent_translations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val boldFont = ResourcesCompat.getFont(activity!!.applicationContext,
            R.font.pt_sans_bold
        )

        textView_recentTranslationsHeader.text = resources.getString(R.string.recent_translations_header)
        textView_recentTranslationsHeader.typeface = boldFont

        button_clearRecent.text = resources.getString(R.string.clear_button)
        button_clearRecent.typeface = boldFont
        button_clearRecent.setOnClickListener(this)

        recycler_recentTranslations.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        recycler_recentTranslations.layoutManager = layoutManager

        if (!translationPresenter.checkForStringInSharedPreferences(resources.getString(R.string.recent_translations))) {
            textView_recentTranslationsHeader.visibility = View.VISIBLE
            recycler_recentTranslations.visibility = View.VISIBLE
            button_clearRecent.visibility = View.VISIBLE
            recentTranslationsArray = translationPresenter.convertJsonStringToArrayList(translationPresenter.getStringFromSharedPreferences(resources.getString(R.string.recent_translations)))
            adapter =
                RecentTranslationsListAdapter(recentTranslationsArray)
            recycler_recentTranslations.adapter = adapter
        }

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(TranslationViewModel::class.java)
        } ?: throw Exception()
        viewModel.recentTranslations.observe(this, Observer<String> { item ->
            recentTranslationsArray = translationPresenter.convertJsonStringToArrayList(item)

            textView_recentTranslationsHeader.visibility = View.VISIBLE
            recycler_recentTranslations.visibility = View.VISIBLE
            button_clearRecent.visibility = View.VISIBLE

            adapter =
                RecentTranslationsListAdapter(recentTranslationsArray)
            recycler_recentTranslations.adapter = adapter
        })
    }

    override fun onClick(v: View) {
        when (v) {
            button_clearRecent -> {
                translationPresenter.deleteStringFromSharedPreferences(resources.getString(R.string.recent_translations))
                textView_recentTranslationsHeader.visibility = View.GONE
                recycler_recentTranslations.visibility = View.GONE
                button_clearRecent.visibility = View.GONE
            }
        }
    }
}
