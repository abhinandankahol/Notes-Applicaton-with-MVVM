package com.example.notesapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapplication.databinding.ActivityAddNotesBinding
import com.example.notesapplication.entities.Notes
import com.example.notesapplication.viewModel.notesVM

@Suppress("DEPRECATION")
class AddNotes : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddNotesBinding.inflate(layoutInflater)
    }
    private lateinit var notes: Notes
    private lateinit var notesViewModel: notesVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        notesViewModel = ViewModelProvider(this)[notesVM::class.java]



        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 2) {
                binding.add.text = "Update Notes"

                notes = intent.getSerializableExtra("oldNotes") as Notes
                binding.notesTitle.setText(notes.titleNotes)
                binding.notesSubTitle.setText(notes.subtitleNotes)
                binding.notesContent.setText(notes.detailedNotes)

                binding.add.setOnClickListener { updateNotes()
                finish()}

            } else {
                binding.add.setOnClickListener { addNotes() }

            }

        } else {
            binding.add.setOnClickListener {
                addNotes()
            }
        }

    }

    fun addNotes() {
         notes = Notes(
            null,
            titleNotes = binding.notesTitle.text.toString(),
            subtitleNotes = binding.notesSubTitle.text.toString(),
            detailedNotes = binding.notesContent.text.toString()
        )
        notesViewModel.createNote(notes)
        finish()
    }

    fun updateNotes() {
        notes = Notes(
            notes.id,
            titleNotes = binding.notesTitle.text.toString(),
            subtitleNotes = binding.notesSubTitle.text.toString(),
            detailedNotes = binding.notesContent.text.toString()

        )
        notesViewModel.updateNotes(notes)
        finish()
    }
}