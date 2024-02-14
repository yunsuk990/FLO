package com.example.flo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(
    @PrimaryKey var id: Int,
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
//    var songs: ArrayList<Song>? = null
)
