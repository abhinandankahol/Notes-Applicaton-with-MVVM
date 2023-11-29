package com.example.notesapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapplication.entities.Notes

@Database(entities = arrayOf(Notes::class), version = 2, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun dao(): com.example.notesapplication.dao.Dao

    companion object {
        var notes_db: NotesDatabase? = null
        fun notes_database(context: Context): NotesDatabase {
            if (notes_db == null) {
                notes_db = Room.databaseBuilder(context, NotesDatabase::class.java, "notes_db").fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()

            }
            return notes_db!!
        }
    }
}