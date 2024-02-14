package com.example.flo.data.remote

data class LastFmResponse(
    val artist: ArtistInfo
)

data class ArtistInfo (
    val name: String,
    val mbid: String,
    val url: String,
    val image: ArrayList<Image>,
    val bio: Info
)

data class Image(
    val text: String,
    val size: String
)

data class Info(
    val published: String,
    val summary: String,
    val content: String,
)

