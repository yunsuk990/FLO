package com.example.flo.data.remote

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.flo.utils.getLastFmRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LastFmService {

    fun getArtistInfo(context: Context, artist: String){
        val lastFmService = getLastFmRetrofit().create(LastFmRetrofitInterface::class.java)
        lastFmService.getArtistInfo(artist = artist).enqueue(object : Callback<LastFmResponse> {
            override fun onResponse(
                call: Call<LastFmResponse>,
                response: Response<LastFmResponse>,
            ) {
                Log.d("LastFmService", "SUCCESS")
                Log.d("LastFmService", response.body()?.artist.toString())
                Toast.makeText(context, response.body()?.artist?.bio?.summary, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<LastFmResponse>, t: Throwable) {

            }

        })
    }
}