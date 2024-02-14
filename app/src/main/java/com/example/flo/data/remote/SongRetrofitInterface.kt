package com.example.flo.data.remote

import com.example.flo.data.remote.SongResponse
import retrofit2.Call
import retrofit2.http.GET

interface SongRetrofitInterface {

    @GET("/songs")
    fun getSongs(): Call<SongResponse>

}