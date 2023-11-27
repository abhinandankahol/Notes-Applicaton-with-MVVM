package com.example.notesapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val notesPosition = notesList[position]
        holder.binding.notesTitle.text = notesPosition.titleNotes
        holder.binding.notesSubTitle.text = notesPosition.subtitleNotes
        holder.binding.notesContent.text = notesPosition.detailedNotes

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AddNotes::class.java)
            intent.putExtra("MODE", 2).putExtra("oldNotes", notesPosition)
            context.startActivity(intent)

        }
    }
}