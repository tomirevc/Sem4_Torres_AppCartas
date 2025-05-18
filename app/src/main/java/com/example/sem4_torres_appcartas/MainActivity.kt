package com.example.sem4_torres_appcartas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNombre: EditText
    private lateinit var botonAceptar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNombre = findViewById(R.id.editTextNombre)
        botonAceptar = findViewById(R.id.botonAceptar)

        botonAceptar.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            if (nombre.isNotEmpty()) {
                val intent = Intent(this, JuegoActivity::class.java)
                intent.putExtra("NOMBRE_USUARIO", nombre)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, ingresa tu nombre", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
