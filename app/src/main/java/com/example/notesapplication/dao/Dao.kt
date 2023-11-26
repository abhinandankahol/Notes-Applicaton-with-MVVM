package com.example.notesapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesapplication.entities.Notes

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createNotes(notes: Notes)

    @Query("SELECT * FROM  notes_table")
    fun readNotes(): LiveData<List<Notes>>

    @Update
    fun updateNotes(notes: Notes)

    @Delete
    fun deleteNotes(notes: Notes)
}