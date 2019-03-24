package com.darkknightsds.romanianenglishtranslator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RecentTranslationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recent_translations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }


}
