package com.example.flo.ui.main.album

import com.example.flo.data.remote.AlbumResult

interface AlbumSongView {
    fun onAlbumSongSuccess(result: ArrayList<AlbumResult>)
    fun onAlbumSongFailure()

    fun onAlbumSongLoading()
}