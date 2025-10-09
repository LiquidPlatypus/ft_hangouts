package com.example.ft_hangouts

import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		val listView = findViewById<ListView>(R.id.listContacts)

		// Inflate header (parent = listView helps pour les LayoutParams)
		val headerView = layoutInflater.inflate(R.layout.header_title, listView, false)
		// Ajouter le header AVANT de setAdapter
		listView.addHeaderView(headerView, null, false)

		// Exemple simple de données + adapter (2 lignes, name + phone)
		val data = ArrayList<Map<String, String>>()
		data.add(hashMapOf("name" to "Alice", "phone" to "0600000000"))
		data.add(hashMapOf("name" to "Bob", "phone" to "0611111111"))

		val from = arrayOf("name", "phone")
		val to = intArrayOf(android.R.id.text1, android.R.id.text2)

		val adapter = SimpleAdapter(this, data, android.R.layout.simple_list_item_2, from, to)
		listView.adapter = adapter

		// Clics (attention à l'offset dû au header)
		listView.setOnItemClickListener { parent, view, position, id ->
			val headerCount = listView.headerViewsCount
			val realPos = position - headerCount
			if (realPos >= 0) {
				// Récupère l'élément (ici via adapter)
				val item = adapter.getItem(realPos) as Map<*, *>
				// ...
			}
		}
	}
}