package com.example.flo.utils

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


const val BASE_URL = "https://edu-api-test.softsquared.com"
const val LastFM_BASE_URL = "https://ws.audioscrobbler.com"

fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    return retrofit
}

fun getLastFmRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl(LastFM_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    return retrofit
}