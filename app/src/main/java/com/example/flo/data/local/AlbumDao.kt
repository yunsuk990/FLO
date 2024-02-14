package com.example.flo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like

@Dao
interface AlbumDao {

    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM Album")
    fun getAlbums(): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun likeAlbum(like: Like)

    @Query("SELECT id FROM LikeTable WHERE userId=:userId AND albumId=:albumId")
    fun isLikedAlbum(userId: Int, albumId: Int): Int?

    @Query("DELETE FROM LikeTable WHERE userId=:userId AND albumId=:albumId")
    fun disLikedAlbum(userId: Int, albumId: Int): Int?


    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN Album as AT ON LT.albumId = AT.id WHERE LT.userId=:userId")
    fun getLikedAlbum(userId: Int): List<Album>

}