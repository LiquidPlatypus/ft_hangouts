package com.example.ft_hangouts

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.Color
import android.view.MenuItem

class ChatActivity : AppCompatActivity() {

	private var contactId: Long = 0L
	private lateinit var messagesDb: MessageDbHelper
	private lateinit var recyclerView: RecyclerView
	private lateinit var adapter: MessageAdapter
	private val handler = Handler(Looper.getMainLooper())

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.input_bar)) { v, insets ->
			val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
			v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, ime.bottom)
			insets
		}

		contactId = intent.getLongExtra("contact_id", 0L)
		if (contactId == 0L) {
			finish()
			return
		}

		messagesDb = MessageDbHelper(this)
		val contactsDb = ContactsDbHelper(this)

		val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		val contact = contactsDb.getContact(contactId)
		if (contact != null) {
			supportActionBar?.title = contact.firstname + " " + contact.lastname
		}

		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		val color = UiColorUtils.getHeaderColor(this)
		UiColorUtils.applyToolbarColor(toolbar, color)
		window.statusBarColor = color
		val controller = WindowInsetsControllerCompat(window, window.decorView)
		val lightIcons = UiColorUtils.getContrastingTextColor(color) == Color.WHITE
		controller.isAppearanceLightStatusBars = !lightIcons

		recyclerView = findViewById(R.id.recycler_chat)
		recyclerView.layoutManager = LinearLayoutManager(this)
		adapter = MessageAdapter()
		recyclerView.adapter = adapter

		val input = findViewById<EditText>(R.id.edit_message)
		val sendBtn = findViewById<Button>(R.id.button_send)

		UiColorUtils.applyButtonColor(sendBtn, color)

		sendBtn.setOnClickListener {
			val text = input.text.toString().trim()
			if (text.isNotEmpty()) {
				val now = System.currentTimeMillis()
				val msg = Message(contactId = contactId, body = text, timestamp = now, isSent = true)
				messagesDb.insertMessage(msg)
				input.text.clear()
				refreshMessages()

				handler.postDelayed({
					val reply = Message(contactId = contactId, body = "Réponse auto à: ${text.take(30)}", timestamp = System.currentTimeMillis(), isSent = false)
					messagesDb.insertMessage(reply)
					refreshMessages()
				}, 1000)
			}
		}

		refreshMessages()
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				finish()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun refreshMessages() {
		val messages = messagesDb.getMessagesForContact(contactId)
		adapter.submitList(messages)
		recyclerView.scrollToPosition(maxOf(0, messages.size - 1))
	}

	override fun onResume() {
		super.onResume()
		val sendBtn = findViewById<Button>(R.id.button_send)

		val color = UiColorUtils.getHeaderColor(this)
		UiColorUtils.applyButtonColor(sendBtn, color)
		val controller = WindowInsetsControllerCompat(window, window.decorView)
		val lightIcons = UiColorUtils.getContrastingTextColor(color) == Color.WHITE
		controller.isAppearanceLightStatusBars = !lightIcons

		refreshMessages()
	}
}