package com.example.notesapplication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesapplication.repository.NotesRepo
import com.example.notesapplication.viewModel.notesVM

class notesVMFactory(private val repo: NotesRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return notesVM(repo) as T
    }
}