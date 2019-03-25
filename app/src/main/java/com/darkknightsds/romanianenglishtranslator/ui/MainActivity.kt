package com.darkknightsds.romanianenglishtranslator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.ext.android.inject
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.darkknightsds.romanianenglishtranslator.R
import kotlinx.android.synthetic.main.activity_main.*

//MainActivity which sets toolbar and launches fragments
class MainActivity : AppCompatActivity() {
    //Values
    private val translationFragment: TranslationFragment by inject()
    private val recentTranslationsFragment: RecentTranslationsFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.app_toolbar)
        setSupportActionBar(toolbar)
        toolbar.changeToolbarFont()
        toolbar.setTitleTextColor(resources.getColor(R.color.white))

        val regFont = ResourcesCompat.getFont(this, R.font.pt_sans_reg)

        textView_yandex.text = resources.getString(R.string.yandex_details)
        textView_yandex.typeface = regFont

        supportFragmentManager.beginTransaction().replace(R.id.translation_fragment, translationFragment).addToBackStack(null).commit()
        supportFragmentManager.beginTransaction().replace(R.id.recent_translations_fragment, recentTranslationsFragment).addToBackStack(null).commit()
    }

    private fun Toolbar.changeToolbarFont(){
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is TextView && view.text == title) {
                view.typeface = ResourcesCompat.getFont(context,
                    R.font.pt_sans_bold
                )
                break
            }
        }
    }
}
