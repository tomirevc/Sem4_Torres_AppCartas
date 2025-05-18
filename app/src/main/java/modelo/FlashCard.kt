package modelo

data class FlashCard(
    val pregunta: String,
    val respuestaCorrecta: Boolean,
    val explicacion: String,
    val imagenResId: Int
)
