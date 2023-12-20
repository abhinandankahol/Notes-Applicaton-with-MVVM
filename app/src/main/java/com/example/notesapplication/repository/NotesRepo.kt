package com.example.notesapplication.repository

import androidx.lifecycle.LiveData
import com.example.notesapplication.dao.Dao
import com.example.notesapplication.entities.Notes

class NotesRepo(private val dao: Dao) {
    suspend fun createNote(notes: Notes) = dao.createNotes(notes)
    fun readNote(): LiveData<List<Notes>> = dao.readNotes()
    suspend fun updateNote(notes: Notes) = dao.updateNotes(notes)
    suspend fun deleteNote(notes: Notes) = dao.deleteNotes(notes)

    fun searchNotes(query: String): LiveData<List<Notes>> = dao.searchNotes("%$query%")

}