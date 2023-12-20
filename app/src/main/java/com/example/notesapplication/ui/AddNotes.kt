package com.example.notesapplication.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.notesapplication.R
import com.example.notesapplication.database.NotesDatabase
import com.example.notesapplication.databinding.ActivityAddNotesBinding
import com.example.notesapplication.databinding.DialogBoxBinding
import com.example.notesapplication.entities.Notes
import com.example.notesapplication.repository.NotesRepo
import com.example.notesapplication.viewModel.notesVM
import com.example.notesapplication.viewmodelfactory.notesVMFactory

@Suppress("DEPRECATION")
class AddNotes : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddNotesBinding.inflate(layoutInflater)
    }
    private lateinit var notes: Notes
    private lateinit var notesViewModel: notesVM
    private var isChangesMade = false

    private val dao = NotesDatabase.notes_database(this).dao()
    private val repo = NotesRepo(dao)

    // Notification channel ID and name
    private val CHANNEL_ID = "notes_channel"
    private val CHANNEL_NAME = "Notes Channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        createNotificationChannel()


        notesViewModel = ViewModelProvider(this, notesVMFactory(repo))[notesVM::class.java]

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
                    Toast.makeText(this@AddNotes, "Edit mode enabled", Toast.LENGTH_SHORT)
                        .show()
                    binding.notesTitle.isEnabled = true
                    binding.notesContent.isEnabled = true
                    binding.add.setImageResource(R.drawable.save_icon)
                    binding.notesTitle.addTextChangedListener { isChangesMade = true }
                    binding.notesContent.addTextChangedListener { isChangesMade = true }

                    binding.add.setOnClickListener {
                        updateNotes()
                        showNotification()
                        Toast.makeText(this, "Note Updated Successfully", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            } else {
                binding.add.setOnClickListener {
                    addNotes()
                    showNotification()
                }
            }
        } else {
            binding.add.setOnClickListener {
                addNotes()

            }
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for Notes Notifications"
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.linkdein)
            .setContentTitle("Note Created")
            .setContentText("Your note has been created successfully!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@AddNotes,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }

    private fun addNotes() {
        val title = binding.notesTitle.text.toString().trim()

        if (title.isEmpty()) {

            Toast.makeText(this, "Notes title cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            notes = Notes(
                null,
                titleNotes = title,
                detailedNotes = binding.notesContent.text.toString()
            )
            showNotification()
            notesViewModel.createNote(notes)
            finish()
        }
    }

    private fun updateNotes() {
        notes = Notes(
            notes.id,
            titleNotes = binding.notesTitle.text.toString(),
            detailedNotes = binding.notesContent.text.toString()
        )
        notesViewModel.updateNotes(notes)
        finish()
    }

    private fun dialog(body: String, buttonName: String) {
        val dialog = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val dialogLay = DialogBoxBinding.inflate(layoutInflater)
        dialog.setView(dialogLay.root)
        if (intent.getIntExtra("MODE", -1) == 2) {
            dialogLay.save.setOnClickListener {
                updateNotes()
                dialog.dismiss()
            }
            dialogLay.discard.setOnClickListener {
                dialog.dismiss()
                finish()
            }
        }
        dialogLay.save.text = buttonName
        dialogLay.saveChanges.text = body
        dialog.show()
    }



    @Deprecated("Deprecated in Java")
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
