package com.example.ft_hangouts

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils

object UiColorUtils {

	fun getHeaderColor(context: Context): Int {
		val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
		val defaultColor = ContextCompat.getColor(context, R.color.header_background)
		return prefs.getInt("header_color", defaultColor)
	}

	fun getContrastingTextColor(backgroundColor: Int): Int {
		return if (ColorUtils.calculateLuminance(backgroundColor) > 0.5) {
			Color.BLACK
		} else {
			Color.WHITE
		}
	}

	fun applyToolbarColor(toolbar: Toolbar, color: Int) {
		toolbar.setBackgroundColor(color)

		val textColor = getContrastingTextColor(color)
		toolbar.setTitleTextColor(textColor)
		toolbar.navigationIcon?.setTint(textColor)

		for (i in 0 until toolbar.menu.size()) {
			toolbar.menu.getItem(i).icon?.setTint(textColor)
		}
	}

	fun applyButtonColor(button: Button, color: Int) {
		button.backgroundTintList = ColorStateList.valueOf(color)
		button.setTextColor(getContrastingTextColor(color))
	}
}