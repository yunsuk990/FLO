package com.example.flo.data.remote

data class AlbumSongsResponse (
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ArrayList<AlbumResult>
)

data class AlbumResult(
    val songIdx: Int,
    val title: String,
    val singer: String,
    val isTitleSong: String
)