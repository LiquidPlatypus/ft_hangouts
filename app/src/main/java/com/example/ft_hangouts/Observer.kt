package com.example.ft_hangouts

interface Observer<T> {
	fun onChanged(data: T)
}