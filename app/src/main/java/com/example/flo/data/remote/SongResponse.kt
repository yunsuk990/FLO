package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

data class SongResponse(
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: Int,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: FloCharResult
)

data class FloCharResult(
    @SerializedName(value = "songs") val songs: ArrayList<FloCharSongs>
)

data class FloCharSongs (
    @SerializedName(value = "songIdx") val songIdx: Int,
    @SerializedName(value = "albumIdx") val albumIdx: Int,
    @SerializedName(value = "singer") val singer: String,
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "coverImgUrl") val coverImgUrl: String
)
