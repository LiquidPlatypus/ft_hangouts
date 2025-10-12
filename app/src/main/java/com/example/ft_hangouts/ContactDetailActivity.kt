package com.example.ft_hangouts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.app.Activity
import android.content.Intent
import com.example.ft_hangouts.database.ContactDatabaseHelper
import com.example.ft_hangouts.model.Contact
import com.example.ft_hangouts.repository.ContactRepository
import com.example.ft_hangouts.viewmodel.ContactViewModel

class ContactDetailActivity: Activity() {
	private lateinit var viewModel: ContactViewModel
	private var currentContact: Contact? = null
	private var isEditMode = false

	private lateinit var nameTextView: TextView
	private lateinit var phoneTextView: TextView
	private lateinit var nameEditText: EditText
	private lateinit var phoneEditText: EditText

	private lateinit var emailEditText: EditText
	private lateinit var addressEditText: EditText
	private lateinit var noteEditText: EditText

	private lateinit var editButton: Button
	private lateinit var deleteButton: Button
	private lateinit var saveButton: Button
	private lateinit var cancelButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_contact_detail)

		// Récupération du ViewModel
		viewModel = getLastNonConfigurationInstance() as? ContactViewModel
			?: run {
				val dbHelper = ContactDatabaseHelper(this)
				val repository = ContactRepository(dbHelper)
				ContactViewModel(repository)
			}

		// Récupération de l'ID du contact depuis l'Intent
		val contactId = intent.getLongExtra("contact_id", -1)
		if (contactId != -1L) {
			currentContact = viewModel.selectedContact.getValue()
			if (currentContact == null) {
				// Si le copntact n'est pas déjà sélectionné, le charger
				viewModel.selectContact(contactId)
			}
		}

		setupViews()
		observeContact()
		displayContact()
	}

	override fun onRetainNonConfigurationInstance(): Any {
		return viewModel
	}

	private fun setupViews() {
		nameTextView = findViewById(R.id.nameTextView)
		phoneTextView = findViewById(R.id.phoneTextView)
		nameEditText = findViewById(R.id.nameEditText)
		phoneEditText = findViewById(R.id.phoneEditText)
		editButton = findViewById(R.id.editButton)
		deleteButton = findViewById(R.id.deleteButton)
		saveButton = findViewById(R.id.saveButton)
		cancelButton = findViewById(R.id.cancelButton)

		editButton.setOnClickListener { enterEditMode() }
		deleteButton.setOnClickListener { deleteContact() }
		saveButton.setOnClickListener { saveContact() }
		cancelButton.setOnClickListener { exitEditMode() }
	}

	private fun observeContact() {
		viewModel.selectedContact.observe(object : Observer<Contact?> {
			override fun onChanged(data: Contact?) {
				currentContact = data
				displayContact()
			}
		})
	}

	private fun displayContact() {
		currentContact?.let { contact ->
			nameTextView.text = contact.name
			phoneTextView.text = contact.phone
			nameEditText.setText(contact.name)
			phoneEditText.setText(contact.phone)
		}
	}

	private fun enterEditMode() {
		isEditMode = true
		nameTextView.visibility = android.view.View.GONE
		phoneTextView.visibility = android.view.View.GONE
		nameEditText.visibility = android.view.View.VISIBLE
		phoneEditText.visibility = android.view.View.VISIBLE
		editButton.visibility = android.view.View.GONE
		deleteButton.visibility = android.view.View.GONE
		saveButton.visibility = android.view.View.VISIBLE
		cancelButton.visibility = android.view.View.VISIBLE
	}

	private fun exitEditMode() {
		isEditMode = false
		nameTextView.visibility = android.view.View.VISIBLE
		phoneTextView.visibility = android.view.View.VISIBLE
		nameEditText.visibility = android.view.View.GONE
		phoneEditText.visibility = android.view.View.GONE
		editButton.visibility = android.view.View.VISIBLE
		deleteButton.visibility = android.view.View.VISIBLE
		saveButton.visibility = android.view.View.GONE
		cancelButton.visibility = android.view.View.GONE
		// Recharger les données en cas d'annulation
		displayContact()
	}

	private fun saveContact() {
		val newName = nameEditText.text.toString().trim()
		val newPhone = phoneEditText.text.toString().trim()
		val newEmail = emailEditText.text.toString().trim()
		val newAddress = addressEditText.text.toString().trim()
		val newNote = noteEditText.text.toString().trim()

		if (newName.isEmpty() || newPhone.isEmpty()) {
			// Vous pourriez afficher un message d'erreur ici
			return
		}

		currentContact?.let { contact ->
			viewModel.updateContact(contact.id, newName, newPhone, newEmail, newAddress, newNote)
			exitEditMode()
		}
	}

	private fun deleteContact() {
		currentContact?.let { contact ->
			viewModel.deleteContact(contact.id)
			// Revenir à la liste après suppression
			finish()
		}
	}
}