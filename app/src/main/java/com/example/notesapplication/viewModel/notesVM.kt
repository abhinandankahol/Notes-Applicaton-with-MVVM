package com.example.notesapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapplication.entities.Notes
import com.example.notesapplication.repository.NotesRepo
import kotlinx.coroutines.launch

class notesVM(private val repository: NotesRepo) : ViewModel() {


    fun createNote(notes: Notes) = viewModelScope.launch { repository.createNote(notes) }
    fun readNotes(): LiveData<List<Notes>> = repository.readNote()
    fun updateNotes(notes: Notes) = viewModelScope.launch { repository.updateNote(notes) }
    fun deleteNote(notes: Notes) = viewModelScope.launch { repository.deleteNote(notes) }

    fun searchNotes(query: String): LiveData<List<Notes>> {
        return repository.searchNotes(query)
    }


}