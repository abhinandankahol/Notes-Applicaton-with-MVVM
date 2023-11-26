package com.example.notesapplication.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
class Notes(
@PrimaryKey(autoGenerate = true)
    var id : Int ?=null,
    var titleNotes: String = "",
    var subtitleNotes: String = "",
    var detailedNotes: String = "",

    ) : Serializable