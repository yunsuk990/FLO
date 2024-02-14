package com.example.flo.ui.main.home

import com.example.flo.data.remote.AlbumCharResult

interface AlbumView {
    fun onAlbumListSuccess(result: AlbumCharResult)
    fun onAlbumListFailure()
}