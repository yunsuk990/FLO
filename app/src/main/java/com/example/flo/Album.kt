package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flo.Database.Song

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null
)
