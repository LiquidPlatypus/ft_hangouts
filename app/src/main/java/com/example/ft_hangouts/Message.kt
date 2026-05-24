package com.example.ft_hangouts

data class Message (
	val id: Long = 0L,
	val contactId: Long,
	val body: String,
	val timestamp: Long,
	val isSent: Boolean
)