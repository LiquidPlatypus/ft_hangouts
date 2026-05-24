package com.example.ft_hangouts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.Color

class AddContactActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_add_contact)

		val toolbar = findViewById<Toolbar>(R.id.toolbar_add)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		val headerColor = UiColorUtils.getHeaderColor(this)
		UiColorUtils.applyToolbarColor(toolbar, headerColor)

		window.statusBarColor = headerColor
		val controller = WindowInsetsControllerCompat(window, window.decorView)
		controller.isAppearanceLightStatusBars = UiColorUtils.getContrastingTextColor(headerColor) == Color.BLACK

		val btnSave = findViewById<Button>(R.id.btn_save_contact)
		UiColorUtils.applyButtonColor(btnSave, headerColor)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_contact_container)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		val editFirstName = findViewById<EditText>(R.id.edit_contact_firstname)
		val editLastName = findViewById<EditText>(R.id.edit_contact_lastname)
		val editPhone = findViewById<EditText>(R.id.edit_contact_phone)
		val editEmail = findViewById<EditText>(R.id.edit_contact_email)
		val editNickname = findViewById<EditText>(R.id.edit_contact_nickname)

		val dbHelper = ContactsDbHelper(this)

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

			val newContact = Contact(firstname = firstname, lastname = lastname, phone = phone, email = email, nickname = nickname)
			val insertedId = dbHelper.insertContact(newContact)

			if (insertedId > 0) {
				Toast.makeText(this, getString(R.string.success_contact_created), Toast.LENGTH_SHORT).show()
				finish()
			} else {
				Toast.makeText(this, getString(R.string.error_contact_creation), Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		finish()
		return true
	}
}