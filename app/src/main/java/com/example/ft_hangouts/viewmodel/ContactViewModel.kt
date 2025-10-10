package com.example.ft_hangouts.viewmodel

class ContactViewModel {
	private val _contacts = Observable<List<Contact>>(emptyList())
	val contacts: Observable<List<Contact>> = _contacts

	init {
		loadInitialContacts()
	}

	private fun loadInitialContacts() {
		val initialContacts = listOf(
			Contact(id = 1, name = "Alice", phone = "0600000000", email = "alice.bou@gmail.com", address = "n'importe où", note = "aaaa"),
			Contact(id = 2, name = "BOB", phone = "06111111111", email = "BOBLEBRICOLEUR@orange.fr", address = "là", note = "bbbb")
		)
		_contacts.setValue(initialContacts)
	}

	fun addContact(name: String, phone: String, email: String, address: String, note: String) {
		val currentList = _contacts.getValue()
		val newContact = Contact(
			id = (currentList.maxOfOrNull { it.id } ?: 0) + 1,
			name = name,
			phone = phone,
			email = email,
			address = address,
			note = note
		)
		_contacts.setValue(currentList + newContact)
	}

	fun deleteContact(contactId: Long) {
		val currentList = _contacts.getValue()
		_contacts.setValue(currentList.filter { it.id != contactId })
	}

	fun updateContact(contactId: Long, newName: String, newPhone: String, newEmail: String, newAddress: String, newNote: String) {
		val currentList = _contacts.getValue()
		_contacts.setValue(currentList.map { contact ->
			if (contact.id == contactId) {
				contact.copy(name = newName, phone = newPhone, email = newEmail, address = newAddress, note = newNote)
			} else {
				contact
			}
		})
	}
}