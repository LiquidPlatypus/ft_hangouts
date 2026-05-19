package com.example.ft_hangouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.Normalizer
import java.util.*

class ContactAdapter(
	private var items: List<ListItem> = emptyList(),
	private val onContactClick: (Contact) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	sealed class ListItem {
		data class Section(val letter: String) : ListItem()
		data class ContactItem(val contact: Contact) : ListItem()
	}

	companion object {
		private const val TYPE_SECTION = 0
		private const val TYPE_CONTACT = 1
	}

	inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val textSection: TextView = itemView.findViewById(R.id.text_section_letter)
	}

	inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val textFullname: TextView = itemView.findViewById(R.id.text_fullname)
	}

	override fun getItemViewType(position: Int): Int {
		return when (items[position]) {
			is ListItem.Section -> TYPE_SECTION
			is ListItem.ContactItem -> TYPE_CONTACT
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return if (viewType == TYPE_SECTION) {
			val view = LayoutInflater.from(parent.context)
				.inflate(R.layout.item_section_header, parent, false)
			SectionViewHolder(view)
		} else {
			val view = LayoutInflater.from(parent.context)
				.inflate(R.layout.item_contact, parent, false)
			ContactViewHolder(view)
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (val item = items[position]) {
			is ListItem.Section -> {
				(holder as SectionViewHolder).textSection.text = item.letter
				holder.itemView.setOnClickListener(null)
			}
			is ListItem.ContactItem -> {
				val contact = item.contact
				val fullname = holder.itemView.context.getString(
					R.string.contact_fullname_format,
					contact.firstname,
					contact.lastname
				).trim()
				(holder as ContactViewHolder).textFullname.text = fullname

				holder.itemView.setOnClickListener {
					val pos = holder.bindingAdapterPosition
					if (pos != RecyclerView.NO_POSITION) {
						onContactClick(contact)
					}
				}
			}
		}
	}

	override fun getItemCount(): Int = items.size

	fun updateData(newContacts: List<Contact>) {
		val sorted = newContacts.sortedWith(compareBy(
			{ (it.firstname + " " + it.lastname).lowercase(Locale.getDefault()) }
		))

		val newItems = mutableListOf<ListItem>()
		var currentSelection: String? = null

		for (c in sorted) {
			val fullname = (c.firstname + " " + c.lastname).trim()
			val letter = firstNormalizedLetter(fullname)
			if (currentSelection == null || currentSelection != letter) {
				currentSelection = letter
				newItems.add(ListItem.Section(letter))
			}
			newItems.add(ListItem.ContactItem(c))
		}

		items = newItems
		notifyDataSetChanged()
	}

	private fun firstNormalizedLetter(name: String): String {
		if (name.isEmpty()) return "#"

		val normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
			.replace(Regex("\\p{InCombiningDiacriticalMarks}"), "")
		val ch = normalized.trim().firstOrNull() ?: return "#"
		val letter = ch.uppercaseChar()
		return if (letter in 'A'..'Z') letter.toString() else "#"
	}
}