package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

data class AlbumListResponse(
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: Int,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: AlbumCharResult
)

data class AlbumCharResult(
    @SerializedName(value = "albums") val albums: ArrayList<AlbumChar>
)

data class AlbumChar(
    @SerializedName(value = "albumIdx") val albumIdx: Int,
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "singer") val singer: String,
    @SerializedName(value = "coverImgUrl") val coverImgUrl: String
)


