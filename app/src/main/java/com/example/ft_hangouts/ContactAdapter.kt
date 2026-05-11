package com.example.ft_hangouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter (
	private var contacts: List<Contact>
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

	class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val textFullname: TextView = itemView.findViewById(R.id.text_fullname)
		val textPhone: TextView = itemView.findViewById(R.id.text_phone)
		val textEmail: TextView = itemView.findViewById(R.id.text_email)
		val textNickname: TextView = itemView.findViewById(R.id.text_nickname)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.item_contact, parent, false)
		return ContactViewHolder(view)
	}

	override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
		val contact = contacts[position]

		holder.textFullname.text = holder.itemView.context.getString(
			R.string.contact_fullname_format,
			contact.firstname,
			contact.lastname
		).trim()
		holder.textPhone.text = contact.phone.ifEmpty { "Téléphone non renseigné" }
		holder.textEmail.text = contact.email.ifEmpty { "Email non renseigné" }
		holder.textNickname.text = contact.nickname.ifEmpty { "Surnom non renseigné" }
	}

	override fun getItemCount(): Int = contacts.size

	fun updateData(newContacts: List<Contact>) {
		contacts = newContacts
		notifyDataSetChanged()
	}
}