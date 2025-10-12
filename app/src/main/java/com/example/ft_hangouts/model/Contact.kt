package com.example.ft_hangouts.model

data class Contact (
	val id: Long = 0,
	val name: String,
	val phone: String,
	val email: String = "",
	val address: String = "",
	val note: String = ""
)