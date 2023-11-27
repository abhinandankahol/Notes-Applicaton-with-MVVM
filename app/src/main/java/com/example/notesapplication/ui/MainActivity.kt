package com.example.notesapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapplication.adapter.NotesAdapter
import com.example.notesapplication.databinding.ActivityMainBinding
import com.example.notesapplication.entities.Notes
import com.example.notesapplication.viewModel.notesVM

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var notesList: ArrayList<Notes>
    private lateinit var adapter: NotesAdapter
    private lateinit var viewModel: notesVM

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddNotes::class.java))
        }

        viewModel = ViewModelProvider(this)[notesVM::class.java]
        viewModel.readNotes().observeForever {
            notesList.clear()
            notesList.addAll(it)
            adapter.notifyDataSetChanged()

        }

        notesList = ArrayList()
        adapter = NotesAdapter(this, notesList)

        binding.notesRV.adapter = adapter
        adapter.notifyDataSetChanged()


    }
}