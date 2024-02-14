package com.example.flo.ui.main.look

import com.example.flo.data.remote.FloCharResult

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: FloCharResult)
    fun onGetSongFailure(code: Int, message: String)
}