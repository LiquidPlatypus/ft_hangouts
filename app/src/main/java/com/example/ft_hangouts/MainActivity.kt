package com.example.ft_hangouts

import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.app.Activity
import com.example.ft_hangouts.viewmodel.ContactViewModel

class Mainactivity : Activity() {
	private lateinit var viewModel: ContactViewModel
	private lateinit var listView: ListView
	private lateinit var adapter: SimpleAdapter

	// L'observer qui réagira aux changements de données
	private val contactsObserver = object : Observer<List<Contact>> {
		override fun onChanged(data: List<Contact>) {
			updateContactsList(data)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		// Récupération du ViewModel existant ou création d'un nouveau
		viewModel = getLastNonConfigurationInstance() as? ContactViewModel
			?: ContactViewModel()

		setupListView()

		// Observation des contacts - dès qu'ils changent, l'UI se met à jour
		viewModel.contacts.observe(contactsObserver)
	}

	override fun onRetainNonConfigurationInstance(): Any {
		// Sauvegarde du ViewModel lors de la rotation
		// Il sera récupéré dans getLastNonConfigurationInstance()
		return viewModel
	}

	override fun onDestroy() {
		super.onDestroy()
		// Nettoyage pour éviter les fuites mémoire
		// On se désabonne seulement si l'Activity est vraiment détruite
		// (pas juste recréée à cause d'une rotation)
		if (isFinishing)
			viewModel.contacts.removeObserver(contactsObserver)
	}

	private fun setupListView() {
		listView = findViewById(R.id.listContacts)

		// Header
		val headerView = layoutInflater.inflate(R.layout.header_title, listView, false)
		listView.addHeaderView(headerView, null, false)

		// Adapter
		val data = ArrayList<Map<String, String>>()
		val from = arrayOf("name", "phone")
		val to = intArrayOf(android.R.id.text1, android.R.id.text2)

		adapter = SimpleAdapter(this, data, android.R.layout.simple_list_item_2, from, to)
		listView.adapter = adapter

		// Gestion des clics
		listView.setOnItemClickListener { parent, view, position, id ->
			val headerCount = listView.headerViewsCount
			val realPos = position - headerCount
			if (realPos >= 0) {
				val item = adapter.getItem(realPos) as Map<*, *>
				// ... TODO: GESTION CLIC
			}
		}
	}

	private fun updateContactsList(contacts: List<Contact>) {
		// Transformation des Contact en Maps pour le SimpleAdapter
		val data = contacts.map { contact ->
			hashMapOf(
				"name" to contact.name,
				"phone" to contact.phone
			)
		}

		// Mise à jour de l'adapter
		adapter.clear()
		adapter.addAll(data)
		adapter.notifyDataSetChanged()
	}
}