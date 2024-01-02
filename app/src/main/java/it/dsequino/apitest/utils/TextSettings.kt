package it.dsequino.apitest.utils

import android.content.Context
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import it.dsequino.apitest.R
import it.dsequino.apitest.models.TextSettingsModel

class TextSettings(context: Context) {
    private var fontId = R.font.sansation_bold
    private var textSettings: TextSettingsModel
    private var textSettingsManager = TextSettingsManager()

    init {
        textSettings = textSettingsManager.loadTextSettings(context) ?: TextSettingsModel(false, 0f)
    }

    fun setFont(view: TextView, type: String) {
        when (type) {
            "bold" -> {
                fontId = if (!textSettings.isDyslexicFont!!) {
                    R.font.sansation_bold
                } else {
                    R.font.open_dyslexic_alta_bold
                }
            }

            "regular" -> {
                fontId = if (!textSettings.isDyslexicFont!!) {
                    R.font.sansation_regular
                } else {
                    R.font.open_dyslexic_regular
                }
            }
        }
        val typeface = ResourcesCompat.getFont(view.context, fontId)
        view.typeface = typeface
    }

    fun setTextSize(view: TextView) {
        val scale = view.resources.displayMetrics.scaledDensity
        val additionalSizePx = textSettings.fontSize?.times(scale)
        view.textSize = (view.textSize + additionalSizePx!!) / scale
    }
}