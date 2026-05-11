package com.example.ft_hangouts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddContactActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_add_contact)

		// Toolbar avec flèche retour
		val toolbar = findViewById<Toolbar>(R.id.toolbar_add)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_contact_container)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		// Récup champs du form
		val editFirstName = findViewById<EditText>(R.id.edit_contact_firstname)
		val editLastName = findViewById<EditText>(R.id.edit_contact_lastname)
		val editPhone = findViewById<EditText>(R.id.edit_contact_phone)
		val editEmail = findViewById<EditText>(R.id.edit_contact_email)
		val editNickname = findViewById<EditText>(R.id.edit_contact_nickname)
		val btnSave = findViewById<Button>(R.id.btn_save_contact)

		btnSave.setOnClickListener {
			val firstname = editFirstName.text.toString().trim()
			val lastname = editLastName.text.toString().trim()
			val phone = editPhone.text.toString().trim()
			val email = editEmail.text.toString().trim()
			val nickname = editNickname.text.toString().trim()

			if (firstname.isEmpty())
				Toast.makeText(this, "Le nom est obligatoire", Toast.LENGTH_SHORT).show()
			else {
				// TODO : sauvegarde dans la DB
				Toast.makeText(this, "Contact '$firstname' créé", Toast.LENGTH_SHORT).show()
				finish() // Retour à MainActivity
			}
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		finish() // Retour à la page précédente
		return true
	}
}