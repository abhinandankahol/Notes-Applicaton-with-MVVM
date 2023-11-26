package com.example.notesapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.notesapplication.database.NotesDatabase
import com.example.notesapplication.entities.Notes
import com.example.notesapplication.repository.NotesRepo

class notesVM(application: Application) : AndroidViewModel(application) {

    val repositoty: NotesRepo

    init {
        val dao = NotesDatabase.notes_database(application).dao()
        repositoty = NotesRepo(dao)
    }

    fun createNote(notes: Notes) = repositoty.createNote(notes)
    fun readNotes(): LiveData<List<Notes>> = repositoty.readNote()
    fun updateNotes(notes: Notes) = repositoty.updateNote(notes)
    fun deleteNote(notes: Notes) = repositoty.deleteNote(notes)

}