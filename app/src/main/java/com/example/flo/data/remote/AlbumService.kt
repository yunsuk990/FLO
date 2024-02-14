package com.example.flo.data.remote

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.main.album.AlbumSongView
import com.example.flo.ui.main.home.AlbumView
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumService {

    private lateinit var albumView: AlbumView
    private lateinit var albumSongView: AlbumSongView

    fun setAlbumView(albumView: AlbumView){
        this.albumView = albumView
    }

    fun setAlbumSongView(albumSongView: AlbumSongView){
        this.albumSongView = albumSongView
    }

    fun getAlbums(){
        val albumService = getRetrofit().create(AlbumRetrofitInterface::class.java)
        albumService.getAlbums().enqueue(object : Callback<AlbumListResponse> {
            override fun onResponse(
                call: Call<AlbumListResponse>,
                response: Response<AlbumListResponse>,
            ) {
                val resp = response.body()!!
                when(resp.code){
                    1000 -> albumView.onAlbumListSuccess(resp.result)
                    else -> albumView.onAlbumListFailure()
                }
            }
            override fun onFailure(call: Call<AlbumListResponse>, t: Throwable) {
                Log.d("AlbumListResponse", "네트워크 오류입니다.")
            }
        })
    }

    fun getAlbumSongs(albumIdx: Int){
        val albumService = getRetrofit().create(AlbumRetrofitInterface::class.java)

        albumSongView.onAlbumSongLoading()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            albumService.getAlbumSongs(albumIdx).enqueue(object : Callback<AlbumSongsResponse>{
                override fun onResponse(
                    call: Call<AlbumSongsResponse>,
                    response: Response<AlbumSongsResponse>,
                ) {
                    val resp = response.body()!!
                    Log.d("getAlbumSongs", resp.toString())
                    when(resp.code){
                        1000 -> albumSongView.onAlbumSongSuccess(resp.result)
                        else -> albumSongView.onAlbumSongFailure()
                    }
                }

                override fun onFailure(call: Call<AlbumSongsResponse>, t: Throwable) {
                    Log.d("getAlbumSongs", "FAILURE")
                }

            })
        }, 500)

    }
}