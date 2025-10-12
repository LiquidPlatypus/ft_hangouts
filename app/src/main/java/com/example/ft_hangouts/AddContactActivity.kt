package com.example.ft_hangouts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.app.Activity
import android.widget.Toast
import com.example.ft_hangouts.database.ContactDatabaseHelper
import com.example.ft_hangouts.repository.ContactRepository
import com.example.ft_hangouts.viewmodel.ContactViewModel

class AddContactActivity : Activity() {
	private lateinit var viewModel: ContactViewModel
	private lateinit var nameEditText: EditText
	private lateinit var phoneEditText: EditText
	private lateinit var emailEditText: EditText
	private lateinit var addressEditText: EditText
	private lateinit var noteEditText: EditText
	private lateinit var saveButton: Button
	private lateinit var cancelButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_contact)

		// Récupération du ViewModel
		viewModel = getLastNonConfigurationInstance() as? ContactViewModel
			?: run {
				val dbHelper = ContactDatabaseHelper(this)
				val repository = ContactRepository(dbHelper)
				ContactViewModel(repository)
			}

		setupViews()
	}

	override fun onRetainNonConfigurationInstance(): Any {
		return viewModel
	}

	private fun setupViews() {
		nameEditText = findViewById(R.id.nameEditText)
		phoneEditText = findViewById(R.id.phoneEditText)
		emailEditText = findViewById(R.id.emailEditText)
		addressEditText = findViewById(R.id.addressEditText)
		noteEditText = findViewById(R.id.noteEditText)

		saveButton = findViewById(R.id.saveButton)
		cancelButton = findViewById(R.id.cancelButton)

		saveButton.setOnClickListener { saveContact() }
		cancelButton.setOnClickListener { cancelAdd() }
	}

	private fun saveContact() {
		val name = nameEditText.text.toString().trim()
		val phone = phoneEditText.text.toString().trim()
		val email = emailEditText.text.toString().trim()
		val address = addressEditText.text.toString().trim()
		val note = noteEditText.text.toString().trim()

		// Validation
		if (name.isEmpty()) {
			Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
			return
		}

		if (phone.isEmpty()) {
			Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
			return
		}

		// Ajouter le contact via le ViewModel
		viewModel.addContact(name, phone, email, address, note)

		// Afficher une confirmation
		Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show()

		// Revenir à la liste principale
		finish()
	}

	private fun cancelAdd() {
		finish()
	}
}