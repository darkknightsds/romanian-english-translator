package com.darkknightsds.romanianenglishtranslator.ui

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.darkknightsds.romanianenglishtranslator.R
import com.darkknightsds.romanianenglishtranslator.Translation

class RecentTranslationsListAdapter(private val data: ArrayList<Translation>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RecentTranslationsListAdapter.RecentTranslationsViewHolder>() {

    class RecentTranslationsViewHolder(val view: View, private val font: Typeface?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val textView = view.findViewById(R.id.textView_recentTranslation) as TextView

        fun bindItems(t: Translation) {
            textView.text = t.en + " : " + t.ro
            textView.typeface = font
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentTranslationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_translation_list_item, parent, false) as View

        val regFont = ResourcesCompat.getFont(parent.context,
            R.font.pt_sans_reg
        )

        return RecentTranslationsViewHolder(
            view,
            regFont
        )
    }

    override fun onBindViewHolder(holder: RecentTranslationsViewHolder, position: Int) {
        holder.bindItems(data[position])
    }

    override fun getItemCount() = data.size
}
