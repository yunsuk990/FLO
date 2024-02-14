package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

val API_KEY = "10b51b77e0d31491fd0bcdcdcadd924c"
interface LastFmRetrofitInterface {

//    "?method=artist.getinfo&artist=IU&api_key=10b51b77e0d31491fd0bcdcdcadd924c&format=json"
    @GET("/2.0/")
    fun getArtistInfo(
        @Query("method") method: String = "artist.getinfo",
        @Query("artist") artist: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("format") format: String = "json"
    ): Call<LastFmResponse>
}