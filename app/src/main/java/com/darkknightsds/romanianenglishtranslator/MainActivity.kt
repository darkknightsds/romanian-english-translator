package com.darkknightsds.romanianenglishtranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.ext.android.inject
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val translationFragment: TranslationFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val regFont = ResourcesCompat.getFont(this, R.font.pt_sans_reg)

        val toolbar = findViewById<Toolbar>(R.id.app_toolbar)
        setSupportActionBar(toolbar)
        toolbar.changeToolbarFont()
        toolbar.setTitleTextColor(resources.getColor(R.color.white))

        textView_yandex.text = resources.getString(R.string.yandex_details)
        textView_yandex.typeface = regFont

        loadFragment(translationFragment)
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.translation_fragment,fragment).addToBackStack(null).commit()
    }
}
