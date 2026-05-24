package com.example.ft_hangouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

private const val TYPE_SENT = 1
private const val TYPE_RECEIVED = 2

class MessageAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(DIFF) {

	override fun getItemViewType(position: Int): Int {
		return if (getItem(position).isSent) TYPE_SENT else TYPE_RECEIVED
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return if (viewType == TYPE_SENT) {
			val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
			SentViewHolder(v)
		} else {
			val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
			ReceivedViewHolder(v)
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val msg = getItem(position)
		val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
		if (holder is SentViewHolder) {
			holder.body.text = msg.body
			holder.time.text = sdf.format(Date(msg.timestamp))
		} else if (holder is ReceivedViewHolder) {
			holder.body.text = msg.body
			holder.time.text = sdf.format(Date(msg.timestamp))
		}
	}

	class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val body: TextView = view.findViewById(R.id.text_message_body)
		val time: TextView = view.findViewById(R.id.text_message_time)
	}

	class ReceivedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val body: TextView = view.findViewById(R.id.text_message_body)
		val time: TextView = view.findViewById(R.id.text_message_time)
	}

	companion object {
		val DIFF = object : DiffUtil.ItemCallback<Message>() {
			override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id
			override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
		}
	}
}