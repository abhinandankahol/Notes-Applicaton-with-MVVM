package com.example.notesapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapplication.R
import com.example.notesapplication.databinding.NotesItemRvBinding
import com.example.notesapplication.entities.Notes
import com.example.notesapplication.ui.AddNotes

class NotesAdapter(var context: Context, var notesList: ArrayList<Notes>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    class NotesViewHolder(val binding: NotesItemRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NotesItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    fun newList(filteredList: ArrayList<Notes>) {
        this.notesList = filteredList
        notifyDataSetChanged()

    }


    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val notesPosition = notesList[position]
        holder.binding.notesTitle.text = notesPosition.titleNotes
        holder.binding.notesContent.text = notesPosition.detailedNotes

        val customColors = intArrayOf(
            ContextCompat.getColor(context, R.color.colour1),
            ContextCompat.getColor(context, R.color.colour2),
            ContextCompat.getColor(context, R.color.colour3),
            ContextCompat.getColor(context, R.color.colour4),
            ContextCompat.getColor(context, R.color.colour5),
            ContextCompat.getColor(context, R.color.colour6),
            ContextCompat.getColor(context, R.color.colour7),
            ContextCompat.getColor(context, R.color.colour8),
            ContextCompat.getColor(context, R.color.colour9),
            ContextCompat.getColor(context, R.color.colour10),
            ContextCompat.getColor(context, R.color.colour11),
            ContextCompat.getColor(context, R.color.colour12)

        )


        val color = customColors[position % customColors.size]
        holder.binding.constraintLay.setBackgroundColor(color)



        holder.binding.notesTitle.setOnClickListener {
            val intent = Intent(context, AddNotes::class.java)
            intent.putExtra("MODE", 2).putExtra("oldNotes", notesPosition)
            context.startActivity(intent)

        }

    }


}