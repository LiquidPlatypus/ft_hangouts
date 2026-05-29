package com.example.ft_hangouts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.Color

class ContactDetailActivity : BaseActivity() {

	private var contactId: Long = 0L
	private lateinit var dbHelper: ContactsDbHelper
	private var contact: Contact? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_contact_detail)

		val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
		setSupportActionBar(toolbar)
		supportActionBar?.title = getString(R.string.details)
		applyHeaderColorFromPrefs()
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contact_detail_container)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		dbHelper = ContactsDbHelper(this)
		contactId = intent.getLongExtra("contact_id", 0L)
		if (contactId == 0L) {
			finish()
			return
		}

		contact = dbHelper.getContact(contactId)
		if (contact == null) {
			finish()
			return
		}

		val c = contact!!
		findViewById<TextView>(R.id.text_detail_name).text = listOf(c.firstname, c.lastname)
			.filter { it.isNotBlank() }
			.joinToString(" ")

		findViewById<TextView>(R.id.text_detail_phone_value).text = c.phone.ifBlank { "—" }
		findViewById<TextView>(R.id.text_detail_email_value).text = c.email.ifBlank { "—" }
		findViewById<TextView>(R.id.text_detail_nickname_value).text = c.nickname.ifBlank { "—" }

		val messageBtn = findViewById<Button?>(R.id.button_message)
		messageBtn?.let { btn ->
			UiColorUtils.applyButtonColor(btn, UiColorUtils.getHeaderColor(this))
			btn.setOnClickListener {
				val intent = Intent(this, ChatActivity::class.java).apply {
					putExtra("contact_id", contactId)
				}
				startActivity(intent)
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_contact_detail, menu)
		val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
		UiColorUtils.applyToolbarColor(toolbar, UiColorUtils.getHeaderColor(this))
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				finish()
				true
			}
			R.id.action_edit_contact -> {
				val intent = Intent(this, EditContactActivity::class.java)
				intent.putExtra("contact_id", contactId)
				startActivity(intent)
				true
			}
			R.id.action_delete_contact -> {
				// Confirmation
				AlertDialog.Builder(this)
					.setMessage(getString(R.string.confirm_delete_contact))
					.setPositiveButton(R.string.delete) { _, _ ->
						val rows = dbHelper.deleteContact(contactId)
						if (rows > 0) {
							Toast.makeText(this, getString(R.string.success_contact_deleted), Toast.LENGTH_SHORT).show()
							finish()
						} else {
							Toast.makeText(this, getString(R.string.error_contact_delete), Toast.LENGTH_SHORT).show()
						}
					}
					.setNegativeButton(android.R.string.cancel, null)
					.show()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onResume() {
		super.onResume()
		contact = dbHelper.getContact(contactId)
		contact?.let {
			findViewById<TextView>(R.id.text_detail_name).text = listOf(it.firstname, it.lastname)
				.filter { part -> part.isNotBlank() }
				.joinToString(" ")
			findViewById<TextView>(R.id.text_detail_phone_value).text = it.phone.ifBlank { "—" }
			findViewById<TextView>(R.id.text_detail_email_value).text = it.email.ifBlank { "—" }
			findViewById<TextView>(R.id.text_detail_nickname_value).text = it.nickname.ifBlank { "—" }
		}
	}

	private fun applyHeaderColorFromPrefs() {
		val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
		val color = UiColorUtils.getHeaderColor(this)
		UiColorUtils.applyToolbarColor(toolbar, color)

		window.statusBarColor = color
		val controller = WindowInsetsControllerCompat(window, window.decorView)
		val lightIcons = UiColorUtils.getContrastingTextColor(color) == Color.WHITE
		controller.isAppearanceLightStatusBars = !lightIcons

		findViewById<Button?>(R.id.button_message)?.let { UiColorUtils.applyButtonColor(it, color) }
	}
}