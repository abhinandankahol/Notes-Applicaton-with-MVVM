package com.example.notesapplication.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapplication.R
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
        binding.info.setOnClickListener {
            showPopUpMenu(it)

        }


        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddNotes::class.java))
        }

        viewModel = ViewModelProvider(this)[notesVM::class.java]
        viewModel.readNotes().observeForever {
            notesList.clear()

            notesList.addAll(it)
            adapter.notifyDataSetChanged()
            if (notesList.isEmpty()) {
                binding.notesRV.visibility = View.INVISIBLE
                binding.empty.visibility = View.VISIBLE
            } else {
                binding.notesRV.visibility = View.VISIBLE
                binding.empty.visibility = View.GONE
            }
        }


        notesList = ArrayList()


        val itemTouchHelper =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        val position = viewHolder.adapterPosition
                        val notesPosition = notesList[position]


                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("Do you want to delete ?")
                        builder.setTitle("Alert !")
                        builder.setCancelable(false)
                        builder.setPositiveButton("Yes") { dialog, which ->
                            notesList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            viewModel.deleteNote(notesPosition)
                            Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT)
                                .show()

                        }
                        builder.setNegativeButton("No") { dialog, which ->
                            adapter.notifyItemChanged(position);

                            dialog.dismiss()
                        }

                        val alertDialog = builder.create()
                        alertDialog.show()

                    }
                }


            }
        val itemTouchHelperRV = ItemTouchHelper(itemTouchHelper)
        itemTouchHelperRV.attachToRecyclerView(binding.notesRV)

        adapter = NotesAdapter(this, notesList)


        binding.notesRV.adapter = adapter


        adapter.notifyDataSetChanged()


    }

    private fun showPopUpMenu(view: View) {
        val popUpMenu = PopupMenu(this, view)
        popUpMenu.inflate(R.menu.icons)
        popUpMenu.setOnMenuItemClickListener {
            val profileUrl: String
            when (it.itemId) {
                R.id.link -> {
                    profileUrl = "https://www.linkedin.com/in/abhinandankahol"

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl))
                    intent.setPackage("com.linkedin.android")

                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {

                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl))
                        startActivity(webIntent)
                    }
                    true
                }

                R.id.whastapp -> {
                    val phoneNum = "+91 7018918026"

                    openWhatsAppChat(this, phoneNum)

                    true
                }

                else -> {
                    false
                }

            }

        }
        popUpMenu.show()
    }

    private fun openWhatsAppChat(context: Context, phoneNumber: String) {
        val number = if (phoneNumber.startsWith("+")) phoneNumber else "+$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$number")

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

            Toast.makeText(context, "WhatsApp is not installed on your device", Toast.LENGTH_SHORT)
                .show()
        }
    }


}


