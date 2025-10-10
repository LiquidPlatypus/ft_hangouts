package com.example.ft_hangouts

class Observable<T>(private var value: T) {
	private val observers = mutableListOf<Observer<T>>()

	fun observe(observer: Observer<T>) {
		observers.add(observer)
		// Notification immédiate avec la valeur actuelle
		observer.onChanged(value)
	}

	fun removeObserver(observer: Observer<T>) {
		observers.remove(observer)
	}

	fun setValue(newValue: T) {
		value = newValue
		notifyObservers()
	}

	fun getValue() = value

	private fun notifyObservers() {
		observers.forEach { it.onChanged(value) }
	}
}