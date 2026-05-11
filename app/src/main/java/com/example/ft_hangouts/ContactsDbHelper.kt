package com.example.ft_hangouts

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactsDbHelper(context: Context) : SQLiteOpenHelper(
	context,
	DATABASE_NAME,
	null,
	DATABASE_VERSION
) {
	override fun onCreate(db: SQLiteDatabase) {
		val createTable = """CREATE TABLE $TABLE_CONTACTS (
								$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
								$COL_FIRSTNAME TEXT NOT NULL,
								$COL_LASTNAME TEXT,
								$COL_PHONE TEXT,
								$COL_EMAIL TEXT,
								$COL_NICKNAME TEXT
								)""".trimIndent()
		db.execSQL(createTable)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
		onCreate(db)
	}

	fun insertContact(contact: Contact): Long {
		val db = writableDatabase
		val values = ContentValues().apply {
			put(COL_FIRSTNAME, contact.firstname)
			put(COL_LASTNAME, contact.lastname)
			put(COL_PHONE, contact.phone)
			put(COL_EMAIL, contact.email)
			put(COL_NICKNAME, contact.nickname)
		}
		val newId = db.insert(TABLE_CONTACTS, null, values)
		db.close()
		return newId
	}

	fun getAllContacts(): List<Contact> {
		val contacts = mutableListOf<Contact>()
		val db = readableDatabase

		val cursor = db.query(
			TABLE_CONTACTS,
			arrayOf(COL_ID, COL_FIRSTNAME, COL_LASTNAME, COL_PHONE, COL_EMAIL, COL_NICKNAME),
			null,
			null,
			null,
			null,
			"$COL_FIRSTNAME ASC"
		)

		cursor.use {
			while (it.moveToNext()) {
				val id = it.getLong(it.getColumnIndexOrThrow(COL_ID))
				val firstname = it.getString(it.getColumnIndexOrThrow(COL_FIRSTNAME))
				val lastname = it.getString(it.getColumnIndexOrThrow(COL_LASTNAME)) ?: ""
				val phone = it.getString(it.getColumnIndexOrThrow(COL_PHONE)) ?: ""
				val email = it.getString(it.getColumnIndexOrThrow(COL_EMAIL)) ?: ""
				val nickname = it.getString(it.getColumnIndexOrThrow(COL_NICKNAME)) ?: ""

				contacts.add(Contact(id = id, firstname = firstname, lastname = lastname, phone = phone, email = email, nickname = nickname))
			}
		}

		db.close()
		return contacts
	}

	companion object {
		private const val DATABASE_NAME = "contacts.db"
		private const val DATABASE_VERSION = 1

		const val TABLE_CONTACTS = "contacts"
		const val COL_ID = "id"
		const val COL_FIRSTNAME = "firstname"
		const val COL_LASTNAME = "lastname"
		const val COL_PHONE = "phone"
		const val COL_EMAIL = "email"
		const val COL_NICKNAME = "nickname"
	}
}