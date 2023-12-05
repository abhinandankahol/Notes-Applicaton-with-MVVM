package com.example.notesapplication.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.notesapplication.R
import com.example.notesapplication.databinding.ActivityAddNotesBinding
import com.example.notesapplication.databinding.DialogBoxBinding
import com.example.notesapplication.entities.Notes
import com.example.notesapplication.viewModel.notesVM

@Suppress("DEPRECATION")
class AddNotes : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddNotesBinding.inflate(layoutInflater)
    }
    private lateinit var notes: Notes
    private lateinit var notesViewModel: notesVM
    private var isChangesMade = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        notesViewModel = ViewModelProvider(this)[notesVM::class.java]

        binding.back.setOnClickListener { onBackPressed() }

        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 2) {
                binding.add.setImageResource(R.drawable.edit_icon)
                binding.notesTitle.isEnabled = false
                binding.notesContent.isEnabled = false
                notes = intent.getSerializableExtra("oldNotes") as Notes
                binding.notesTitle.setText(notes.titleNotes)
                binding.notesContent.setText(notes.detailedNotes)

                binding.add.setOnClickListener {
                    Toast.makeText(this@AddNotes, "Edit mode enabled", Toast.LENGTH_SHORT).show()
                    binding.notesTitle.isEnabled = true
                    binding.notesContent.isEnabled = true
                    binding.add.setImageResource(R.drawable.save_icon)
                    binding.notesTitle.addTextChangedListener { isChangesMade = true }
                    binding.notesContent.addTextChangedListener { isChangesMade = true }

                    binding.add.setOnClickListener {

                        updateNotes()
                        Toast.makeText(this, "Note Updated Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }


                }

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
            detailedNotes = binding.notesContent.text.toString()
        )
        notesViewModel.createNote(notes)
        finish()
    }

    fun updateNotes() {
        notes = Notes(
            notes.id,
            titleNotes = binding.notesTitle.text.toString(),
            detailedNotes = binding.notesContent.text.toString()
        )
        notesViewModel.updateNotes(notes)
        finish()
    }

    fun dialog(body: String, buttonName: String) {
        val dialog = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val dialogLay = DialogBoxBinding.inflate(layoutInflater)
        dialog.setView(dialogLay.root)
        if (intent.getIntExtra("MODE", -1) == 2) {
            dialogLay.save.setOnClickListener {
                updateNotes()
                dialog.dismiss()

            }
            dialogLay.discard.setOnClickListener { dialog.dismiss()
            finish()}

        }
        dialogLay.save.text = buttonName
        dialogLay.saveChanges.text = body
        dialog.show()
    }

    override fun onBackPressed() {
        if (isChangesMade) {
            val body = "Are you sure, want to discard changes ?"
            val buttonName = "Keep"
            dialog(body, buttonName)

        } else {
            super.onBackPressed()
        }
    }


}