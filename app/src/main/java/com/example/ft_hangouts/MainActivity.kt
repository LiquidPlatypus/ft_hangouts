package com.example.ft_hangouts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

	private lateinit var contactAdapter: ContactAdapter
	private lateinit var dbHelper: ContactsDbHelper

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)

		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		applyHeaderColorFromPrefs()

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