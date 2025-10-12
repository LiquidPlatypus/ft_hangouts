package com.example.ft_hangouts.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

	companion object {
		private const val DATABASE_NAME = "contacts.db"
		private const val DATABASE_VERSION = 1

		const val TABLE_CONTACTS = "contacts"
		const val COLUMN_ID = "id"
		const val COLUMN_NAME = "name"
		const val COLUMN_PHONE = "phone"
		const val COLUMN_EMAIL = "email"
		const val COLUMN_ADDRESS = "address"
		const val COLUMN_NOTE = "note"
	}

	override fun onCreate(db: SQLiteDatabase) {
		// Création de la table au premier lancement de l'app
		val createTableSQL = """
			CREATE TABLE $TABLE_CONTACTS (
				$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
				$COLUMN_NAME TEXT,
				$COLUMN_PHONE TEXT,
				$COLUMN_EMAIL TEXT,
				$COLUMN_ADDRESS TEXT,
				$COLUMN_NOTE TEXT
			)
		""".trimIndent()

		db.execSQL(createTableSQL)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		// Cette méthode est appelée si vous augmentez DATABASE_VERSION
		// Pour l'instant, on supprime l'ancienne table et on en crée une nouvelle
		db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
		onCreate(db)
	}
}