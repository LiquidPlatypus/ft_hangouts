package com.example.ft_hangouts.viewmodel

import com.example.ft_hangouts.model.Contact
import com.example.ft_hangouts.Observer
import com.example.ft_hangouts.Observable
import com.example.ft_hangouts.repository.ContactRepository

class ContactViewModel(private val repository: ContactRepository) {
	private val _contacts = Observable<List<Contact>>(emptyList())
	val contacts: Observable<List<Contact>> = _contacts

	private val _selectedContact = Observable<Contact?>(null)
	val selectedContact: Observable<Contact?> = _selectedContact

	init {
		loadContacts()
	}

	private fun loadContacts() {
		val contacts = repository.getAllContacts()
		_contacts.setValue(contacts)
	}

	// Ajouter un contact
	fun addContact(name: String, phone: String, email: String, address: String, note: String) {
		val newId = repository.addContact(name, phone)
		// Recharger la liste complète pour garder les données synchronisées
		loadContacts()
	}

	// Mettre à jour un contact
	fun updateContact(id: Long, name: String, phone: String, email: String, address: String, note: String) {
		repository.updateContact(id, name, phone, email, address, note)
		// Recharger la liste
		loadContacts()
		// SI c'était le contact sélectionnée, le mettre à jour aussi
		if (_selectedContact.getValue()?.id == id) {
			selectContact(id)
		}
	}

	// Supprimer un contact
	fun deleteContact(id: Long) {
		repository.deleteContact(id)
		// Recharger la liste
		loadContacts()
		// Si c'était le contact sélectionné, le désélectionner
		if (_selectedContact.getValue()?.id == id) {
			_selectedContact.setValue(null)
		}
	}

	// Sélectionner un contact pour voir ses détails
	fun selectContact(id: Long) {
		val contact = repository.getContactById(id)
		_selectedContact.setValue(contact)
	}

	// Désélectionner le contact actuel
	fun clearSelectedContact() {
		_selectedContact.setValue(null)
	}
}