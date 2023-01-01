package com.example.flo.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.Album

@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 3)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun UserDao(): UserDao
    abstract fun AlbumDao(): AlbumDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }
    }
}