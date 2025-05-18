package com.example.sem4_torres_appcartas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import modelo.FlashCard

class FlashCardAdapter(private val flashCardsOriginal: List<FlashCard>) :
    RecyclerView.Adapter<FlashCardAdapter.FlashCardViewHolder>() {

    private val flashCards = flashCardsOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flash_card, parent, false)
        return FlashCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlashCardViewHolder, position: Int) {
        val card = flashCards[position]
        holder.preguntaText.text = card.pregunta
        holder.imagen.setImageResource(card.imagenResId)
    }

    override fun getItemCount(): Int = flashCards.size

    fun removeCard(position: Int) {
        if (position < flashCards.size) {
            flashCards.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class FlashCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val preguntaText: TextView = itemView.findViewById(R.id.textPregunta)
        val imagen: ImageView = itemView.findViewById(R.id.imageFlashCard)
    }
}
