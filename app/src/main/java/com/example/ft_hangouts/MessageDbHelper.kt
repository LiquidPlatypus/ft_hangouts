package com.example.ft_hangouts

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MessageDbHelper(context: Context) : SQLiteOpenHelper(
	context,
	DATABASE_NAME,
	null,
	DATABASE_VERSION
) {
	override fun onCreate(db: SQLiteDatabase) {
		val create = """CREATE TABLE $TABLE_MESSAGES (
							$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
							$COL_CONTACT_ID INTEGER NOT NULL,
							$COL_BODY TEXT NOT NULL,
							$COL_TIMESTAMP INTEGER NOT NULL,
							$COL_IS_SENT INTEGER NOT NULL
							)""".trimIndent()
		db.execSQL(create)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
		onCreate(db)
	}

	fun insertMessage(message: Message): Long {
		val db = writableDatabase
		val values = ContentValues().apply {
			put(COL_CONTACT_ID, message.contactId)
			put(COL_BODY, message.body)
			put(COL_TIMESTAMP, message.timestamp)
			put(COL_IS_SENT, if (message.isSent) 1 else 0)
		}
		val id = db.insert(TABLE_MESSAGES, null, values)
		db.close()
		return id
	}

	fun getMessagesForContact(contactId: Long): List<Message> {
		val list = mutableListOf<Message>()
		val db = readableDatabase
		val cursor = db.query(
			TABLE_MESSAGES,
			arrayOf(COL_ID, COL_CONTACT_ID, COL_BODY, COL_TIMESTAMP, COL_IS_SENT),
			"$COL_CONTACT_ID = ?",
			arrayOf(contactId.toString()),
			null,
			null,
			"$COL_TIMESTAMP ASC"
		)
		cursor.use {
			while (it.moveToNext()) {
				val id = it.getLong(it.getColumnIndexOrThrow(COL_ID))
				val cid = it.getLong(it.getColumnIndexOrThrow(COL_CONTACT_ID))
				val body = it.getString(it.getColumnIndexOrThrow(COL_BODY))
				val ts = it.getLong(it.getColumnIndexOrThrow(COL_TIMESTAMP))
				val isSent = it.getInt(it.getColumnIndexOrThrow(COL_IS_SENT)) == 1
				list.add(Message(id = id, contactId = cid, body = body, timestamp = ts, isSent = isSent))
			}
		}
		db.close()
		return list
	}

	companion object {
		private const val DATABASE_NAME = "messages.db"
		private const val DATABASE_VERSION = 1

		const val TABLE_MESSAGES = "messages"
		const val COL_ID = "id"
		const val COL_CONTACT_ID = "contact_id"
		const val COL_BODY = "body"
		const val COL_TIMESTAMP = "timestamp"
		const val COL_IS_SENT = "is_sent"
	}
}