package com.example.notesapplication.repository

import androidx.lifecycle.LiveData
import com.example.notesapplication.dao.Dao
import com.example.notesapplication.entities.Notes

class NotesRepo(val dao: Dao) {
    fun createNote(notes: Notes) = dao.createNotes(notes)
    fun readNote(): LiveData<List<Notes>> = dao.readNotes()
    fun updateNote(notes: Notes) = dao.updateNotes(notes)
    fun deleteNote(notes: Notes) = dao.deleteNotes(notes)
}