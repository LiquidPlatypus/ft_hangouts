package com.example.ft_hangouts

data class Contact(
	val id: Long = 0L,
	val firstname: String,
	val lastname: String,
	val phone: String,
	val email: String,
	val nickname: String
)
