package com.example.ft_hangouts.repository

import android.content.ContentValues
import com.example.ft_hangouts.database.ContactDatabaseHelper
import com.example.ft_hangouts.database.ContactDatabaseHelper.Companion.COLUMN_ID
import com.example.ft_hangouts.database.ContactDatabaseHelper.Companion.COLUMN_NAME
import com.example.ft_hangouts.database.ContactDatabaseHelper.Companion.COLUMN_PHONE
import com.example.ft_hangouts.database.ContactDatabaseHelper.Companion.COLUMN_EMAIL
import com.example.ft_hangouts.database.ContactDatabaseHelper.Companion.COLUMN_ADDRESS
import com.example.ft_hangouts.database.ContactDatabaseHelper.Companion.COLUMN_NOTE
import com.example.ft_hangouts.database.ContactDatabaseHelper.Companion.TABLE_CONTACTS
import com.example.ft_hangouts.model.Contact

class ContactRepository(private val dbHelper: ContactDatabaseHelper) {

	// Récupérer tous les contacts de la base de données
	fun getAllContacts(): List<Contact> {
		val contacts = mutableListOf<Contact>()
		val db = dbHelper.readableDatabase

		val cursor = db.query(
			TABLE_CONTACTS,           // Table
			arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_ADDRESS, COLUMN_NOTE), // Colonnes à récupérer
			null,                 // Selection (WHERE)
			null,             // Arguments pour la sélection
			null,                 // Group by
			null,                  // Having
			null                  // Order by
		)

		// Parcourir les résultats du curseur
		if (cursor.moveToFirst()) {
			do {
				val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
				val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
				val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
				val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))?: ""
				val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))?: ""
				val note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))?: ""

				contacts.add(Contact(id, name, phone, email, address, note))
			} while (cursor.moveToNext())
		}
		cursor.close()

		return contacts
	}

	// Récupérer un contact spécifique par son ID
	fun getContactById(id: Long): Contact? {
		val db = dbHelper.readableDatabase

		val cursor = db.query(
			TABLE_CONTACTS,
			arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_PHONE),
			"$COLUMN_ID = ?",          // Selection
			arrayOf(id.toString()),    // Arguments
			null,
			null,
			null
		)

		var contact: Contact? = null
		if (cursor.moveToFirst()) {
			val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
			val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
			val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
			val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))?: ""
			val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))?: ""
			val note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))?: ""

			contact = Contact(id = contactId, name = name, phone = phone, email = email, address = address, note = note)
		}
		cursor.close()

		return contact
	}

	// Ajouter un contact à la base de données
	fun addContact(name: String, phone: String, email: String? = null, address: String?  = null, note: String? = null): Long {
		val db = dbHelper.writableDatabase

		val values = ContentValues().apply {
			put(COLUMN_NAME, name)
			put(COLUMN_PHONE, phone)
			put(COLUMN_EMAIL, email ?: "")
			put(COLUMN_ADDRESS, address ?: "")
			put(COLUMN_NOTE, note ?: "")
		}

		// insert() retourne l'ID du nouvel enregistrement
		return db.insert(TABLE_CONTACTS, null, values)
	}

	// Mettre à jour un contact existant
	fun updateContact(id: Long, name: String, phone: String, email: String, address: String, note: String): Boolean {
		val db = dbHelper.writableDatabase

		val values = ContentValues().apply {
			put(COLUMN_NAME, name)
			put(COLUMN_PHONE, phone)
			put(COLUMN_EMAIL, email)
			put(COLUMN_ADDRESS, address)
			put(COLUMN_NOTE, note)
		}

		// update() retourne le nombre de lignes affectées
		val rowsUpdated = db.update(
			TABLE_CONTACTS,
			values,
			"$COLUMN_ID = ?",
			arrayOf(id.toString())
		)

		return rowsUpdated > 0
	}

	// Supprime un contact
	fun deleteContact(i: Long): Boolean {
		val db = dbHelper.writableDatabase

		val rowsDeleted = db.delete(
			TABLE_CONTACTS,
			"$COLUMN_ID = ?",
			arrayOf(i.toString())
		)

		return rowsDeleted > 0
	}
}