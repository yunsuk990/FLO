package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumRetrofitInterface {

    @GET("/albums")
    fun getAlbums(): Call<AlbumListResponse>

    @GET("albums/{albumIdx}")
    fun getAlbumSongs(
        @Path(value = "albumIdx") albumIdx: Int
    ): Call<AlbumSongsResponse>
}