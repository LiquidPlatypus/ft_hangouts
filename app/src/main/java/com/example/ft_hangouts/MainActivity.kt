package com.example.ft_hangouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

	private lateinit var contactAdapter: ContactAdapter
	private lateinit var dbHelper: ContactsDbHelper

	companion object {
		private const val PREFS_NAME = "app_state"
		private const val KEY_BACKGROUND_TIMESTAMP = "background_timestamp"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)

		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		applyHeaderColorFromPrefs()

		getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
			.edit()
			.apply()

		supportFragmentManager.setFragmentResultListener(
			ColorPickerDialogFragment.RESULT_KEY, this
		) { _, bundle ->
			val color = bundle.getInt("color")
			getSharedPreferences("settings", Context.MODE_PRIVATE)
				.edit().putInt("header_color", color).apply()
			applyHeaderColorFromPrefs()
		}

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		dbHelper = ContactsDbHelper(this)

		val recyclerView = findViewById<RecyclerView>(R.id.recycler_contacts)
		recyclerView.layoutManager = LinearLayoutManager(this)
		contactAdapter = ContactAdapter(emptyList()) { contact ->
			val intent = Intent(this, ContactDetailActivity::class.java)
			intent.putExtra("contact_id", contact.id)
			startActivity(intent)
		}
		recyclerView.adapter = contactAdapter
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

	override fun onResume() {
		super.onResume()
		applyHeaderColorFromPrefs()
		loadContacts()
	}

	private fun loadContacts() {
		val contacts = dbHelper.getAllContacts()
		contactAdapter.updateData(contacts)
	}

	private fun applyHeaderColorFromPrefs() {
		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		val color = UiColorUtils.getHeaderColor(this)
		UiColorUtils.applyToolbarColor(toolbar, color)
	}

	private fun showHSVColorPicker() {
		val frag = ColorPickerDialogFragment.newInstance()
		frag.show(supportFragmentManager, "color_picker")
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_toolbar, menu)
		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		UiColorUtils.applyToolbarColor(toolbar, UiColorUtils.getHeaderColor(this))
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_add_contact -> {
				val intent = Intent(this, AddContactActivity::class.java)
				startActivity(intent)
				true
			}
			R.id.action_change_header -> {
				showHSVColorPicker()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}