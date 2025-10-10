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
	}
}