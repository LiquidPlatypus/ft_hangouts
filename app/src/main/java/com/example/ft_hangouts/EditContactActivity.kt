package com.example.ft_hangouts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditContactActivity : BaseActivity() {

	private lateinit var dbHelper: ContactsDbHelper
	private var contactId: Long = 0L

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_edit_contact)

		val toolbar = findViewById<Toolbar>(R.id.toolbar_edit)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.title = getString(R.string.edit)

		val scroll = findViewById<ScrollView>(R.id.scroll_edit_contact)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_contact_container)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			val ime = insets.getInsets(WindowInsetsCompat.Type.ime())

			v.setPadding(
				systemBars.left,
				systemBars.top,
				systemBars.right,
				systemBars.bottom
			)

			scroll.setPadding(
				scroll.paddingLeft,
				scroll.paddingTop,
				scroll.paddingRight,
				ime.bottom
			)

			insets
		}

		val editFirstName = findViewById<EditText>(R.id.edit_contact_firstname)
		val editLastName = findViewById<EditText>(R.id.edit_contact_lastname)
		val editPhone = findViewById<EditText>(R.id.edit_contact_phone)
		val editEmail = findViewById<EditText>(R.id.edit_contact_email)
		val editNickname = findViewById<EditText>(R.id.edit_contact_nickname)
		val btnSave = findViewById<Button>(R.id.btn_save_contact)

		val headerColor = UiColorUtils.getHeaderColor(this)
		UiColorUtils.applyToolbarColor(toolbar, headerColor)
		UiColorUtils.applyButtonColor(btnSave, headerColor)

		dbHelper = ContactsDbHelper(this)

		contactId = intent.getLongExtra("contact_id", 0L)
		if (contactId == 0L) {
			Toast.makeText(this, getString(R.string.contact_not_found), Toast.LENGTH_SHORT).show()
			finish()
			return
		}

		val contact = dbHelper.getContact(contactId)
		if (contact == null) {
			Toast.makeText(this, getString(R.string.contact_not_found), Toast.LENGTH_SHORT).show()
			finish()
			return
		}

		editFirstName.setText(contact.firstname)
		editLastName.setText(contact.lastname)
		editPhone.setText(contact.phone)
		editEmail.setText(contact.email)
		editNickname.setText(contact.nickname)

		btnSave.setOnClickListener {
			val firstname = editFirstName.text.toString().trim()
			val lastname = editLastName.text.toString().trim()
			val phone = editPhone.text.toString().trim()
			val email = editEmail.text.toString().trim()
			val nickname = editNickname.text.toString().trim()

			if (firstname.isEmpty()) {
				Toast.makeText(this, getString(R.string.error_firstname_required), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			val updatedContact = Contact(
				id = contactId,
				firstname = firstname,
				lastname = lastname,
				phone = phone,
				email = email,
				nickname = nickname
			)

			val rows = dbHelper.updateContact(updatedContact)
			if (rows > 0) {
				Toast.makeText(this, getString(R.string.success_contact_updated), Toast.LENGTH_SHORT).show()
				finish()
			} else {
				Toast.makeText(this, getString(R.string.error_contact_update), Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onResume() {
		super.onResume()
		val toolbar = findViewById<Toolbar>(R.id.toolbar_edit)
		val btnSave = findViewById<Button>(R.id.btn_save_contact)
		val headerColor = UiColorUtils.getHeaderColor(this)
		UiColorUtils.applyToolbarColor(toolbar, headerColor)
		UiColorUtils.applyButtonColor(btnSave, headerColor)
	}

	override fun onSupportNavigateUp(): Boolean {
		finish()
		return true
	}
}