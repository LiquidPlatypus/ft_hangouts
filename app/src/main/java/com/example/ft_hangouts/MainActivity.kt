package com.example.ft_hangouts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

	private lateinit var contactAdapter: ContactAdapter
	private lateinit var dbHelper: ContactsDbHelper

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)

		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		dbHelper = ContactsDbHelper(this)

		val recyclerView = findViewById<RecyclerView>(R.id.recycler_contacts)
		recyclerView.layoutManager = LinearLayoutManager(this)
		contactAdapter = ContactAdapter(emptyList())
		recyclerView.adapter = contactAdapter
	}

	override fun onResume() {
		super.onResume()
		loadContacts()
	}

	private fun loadContacts() {
		val contacts = dbHelper.getAllContacts()
		contactAdapter.updateData(contacts)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_toolbar, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_add_contact -> {
				val intent = Intent(this, AddContactActivity::class.java)
				startActivity(intent)
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}