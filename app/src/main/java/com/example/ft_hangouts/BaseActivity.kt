package com.example.ft_hangouts

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

	companion object {
		protected const val PREFS_NAME = "app_state"
		protected const val KEY_BACKGROUND_TIMESTAMP = "background_timestamp"
	}

	override fun onStart() {
		super.onStart()

		val app = application as MyApplication
		if (!app.consumeBackgroundReturnEvent()) return

		val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
		val timestamp = prefs.getLong(KEY_BACKGROUND_TIMESTAMP, -1L)

		if (timestamp != -1L) {
			val formattedTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
				.format(Date(timestamp))

			Toast.makeText(
				this,
				getString(R.string.background_time_toast, formattedTime),
				Toast.LENGTH_SHORT
			).show()
		}
	}

	override fun onStop() {
		super.onStop()

		getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
			.edit()
			.putLong(KEY_BACKGROUND_TIMESTAMP, System.currentTimeMillis())
			.apply()
	}
}