package com.example.sem4_torres_appcartas

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class JuegoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FlashCardAdapter
    private lateinit var botonTerminar: Button
    private lateinit var nombreUsuario: String
    private lateinit var textProgreso: TextView

    // Lista de flashcards
    private val flashCards = mutableListOf(
        modelo.FlashCard(
            "¿Es obligatorio usar Kotlin para desarrollar apps en Android Studio?",
            false,
            "Aunque Kotlin es recomendado, Android Studio también admite Java y otros lenguajes.",
            R.drawable.pregunta01
        ),
        modelo.FlashCard(
            "¿El archivo AndroidManifest.xml define las actividades y permisos de una app?",
            true,
            "El AndroidManifest.xml declara componentes como actividades, servicios y permisos requeridos.",
            R.drawable.pregunta02
        ),
        modelo.FlashCard(
            "¿La carpeta res/layout contiene los archivos de código Kotlin?",
            false,
            "Los archivos Kotlin están en src, mientras que res/layout contiene archivos XML para las vistas.",
            R.drawable.pregunta03
        ),
        modelo.FlashCard(
            "¿Una Activity representa una sola pantalla en una app Android?",
            true,
            "Cada Activity en Android representa una interfaz visual individual para el usuario.",
            R.drawable.pregunta04
        ),
        modelo.FlashCard(
            "¿El archivo build.gradle se usa para configurar la interfaz de usuario?",
            false,
            "build.gradle configura dependencias y versiones, no el diseño visual.",
            R.drawable.pregunta05
        ),
        modelo.FlashCard(
            "¿La clase MainActivity.kt es usualmente la entrada principal de una app Android?",
            true,
            "MainActivity suele ser la primera pantalla mostrada al iniciar la app.",
            R.drawable.pregunta06
        ),
        modelo.FlashCard(
            "¿Los botones se definen exclusivamente en Kotlin?",
            false,
            "Los botones se definen normalmente en XML y luego se enlazan con Kotlin.",
            R.drawable.pregunta07
        ),
        modelo.FlashCard(
            "¿El emulador de Android Studio permite probar apps sin un dispositivo físico?",
            true,
            "El emulador simula dispositivos para probar apps directamente desde Android Studio.",
            R.drawable.pregunta08
        ),
        modelo.FlashCard(
            "¿Se puede usar SQLite en Android para guardar datos?",
            true,
            "SQLite es una base de datos local integrada en Android.",
            R.drawable.pregunta09
        ),
        modelo.FlashCard(
            "¿Un LinearLayout puede contener múltiples elementos de forma vertical u horizontal?",
            true,
            "LinearLayout organiza elementos en una sola fila o columna.",
            R.drawable.pregunta10
        ),
        modelo.FlashCard(
            "¿Se puede cambiar el idioma de la app usando archivos XML?",
            true,
            "Los recursos localizados se definen en archivos strings.xml en carpetas por idioma.",
            R.drawable.pregunta11
        ),
        modelo.FlashCard(
            "¿ConstraintLayout es menos flexible que LinearLayout?",
            false,
            "ConstraintLayout es más flexible y permite posicionamientos complejos.",
            R.drawable.pregunta12
        ),
        modelo.FlashCard(
            "¿Las apps Android no pueden acceder a Internet sin permiso en el manifiesto?",
            true,
            "Se debe declarar el permiso INTERNET en el AndroidManifest.xml.",
            R.drawable.pregunta13
        ),
        modelo.FlashCard(
            "¿Kotlin es un lenguaje orientado a objetos y funcional?",
            true,
            "Kotlin admite ambos paradigmas: orientado a objetos y programación funcional.",
            R.drawable.pregunta14
        ),
        modelo.FlashCard(
            "¿Un RecyclerView se usa para mostrar una lista simple sin interacción?",
            false,
            "RecyclerView permite listas complejas con alta personalización e interacción.",
            R.drawable.pregunta15
        ),
        modelo.FlashCard(
            "¿La carpeta drawable contiene imágenes y recursos gráficos?",
            true,
            "En drawable se guardan imágenes como PNG, JPG, SVG, etc.",
            R.drawable.pregunta16
        ),
        modelo.FlashCard(
            "¿Una app Android solo puede tener una actividad?",
            false,
            "Una app puede tener múltiples actividades conectadas entre sí.",
            R.drawable.pregunta17
        ),
        modelo.FlashCard(
            "¿onCreate es el primer método que se ejecuta en una actividad?",
            true,
            "onCreate es llamado al crear la actividad y se usa para inicializar componentes.",
            R.drawable.pregunta18
        ),
        modelo.FlashCard(
            "¿Se puede usar Java y Kotlin juntos en el mismo proyecto Android?",
            true,
            "Kotlin y Java pueden coexistir y llamarse entre sí en una app Android.",
            R.drawable.pregunta19
        ),
        modelo.FlashCard(
            "¿El archivo strings.xml se usa para definir colores en la app?",
            false,
            "Los colores se definen en colors.xml, no en strings.xml.",
            R.drawable.pregunta20
        )
    ).apply {
        shuffle()  //desordenar la lista
    }


    private val totalPreguntas = flashCards.size
    private var puntos = 0
    private var contadorRespuestas = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO") ?: "Jugador"

        textProgreso = findViewById(R.id.textProgreso)
        recyclerView = findViewById(R.id.recyclerViewFlashCards)
        botonTerminar = findViewById(R.id.botonTerminar)

        adapter = FlashCardAdapter(flashCards)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        actualizarProgreso()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // Solo permitir swipe en la primera carta
                if (position != 0) {
                    adapter.notifyItemChanged(position)
                    Toast.makeText(this@JuegoActivity, "Por favor desliza la primera flashcard", Toast.LENGTH_SHORT).show()
                    return
                }

                val card = flashCards[0]
                val respuestaUsuario = direction == ItemTouchHelper.RIGHT
                val correcto = respuestaUsuario == card.respuestaCorrecta

                if (correcto) puntos++

                mostrarResultado(correcto, card.explicacion)

                flashCards.removeAt(0)
                adapter.removeCard(0)
                contadorRespuestas++
                actualizarProgreso()
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        botonTerminar.setOnClickListener {
            mostrarResumenFinal()
        }
    }

    private fun actualizarProgreso() {
        textProgreso.text = "Pregunta $contadorRespuestas de $totalPreguntas"
    }

    private fun mostrarResultado(correcto: Boolean, explicacion: String) {
        val mensaje = if (correcto) "¡Correcto!" else "Incorrecto."
        val color = if (correcto) Color.GREEN else Color.RED

        val spannable = SpannableString(mensaje)
        spannable.setSpan(ForegroundColorSpan(color), 0, mensaje.length, 0)

        AlertDialog.Builder(this)
            .setTitle(spannable)
            .setMessage(explicacion)
            .setPositiveButton("Continuar") { dialog, _ ->
                dialog.dismiss()
                if (contadorRespuestas >= totalPreguntas) {
                    mostrarResumenFinal()
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun mostrarResumenFinal() {
        val mensaje = "$nombreUsuario, tu puntuación fue: $puntos de $totalPreguntas"
        val titulo = SpannableString("Juego terminado")
        titulo.setSpan(ForegroundColorSpan(Color.BLUE), 0, titulo.length, 0)

        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
