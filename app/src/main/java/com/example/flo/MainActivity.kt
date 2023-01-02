package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.Database.Song
import com.example.flo.Database.SongDatabase
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var song: Song = Song()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()
        //Bottom Navigation 설정
        initBottomNavigation()

        //음악 초기화
        //val song = Song(binding.mainSongTitleTv.text.toString(), binding.mainSongSingerTv.text.toString(), 0, 60, false, "music_lilac")

        binding.mainPlayerCl.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        Log.d("MAIN?HWT_TO_SERVER", getJwt().toString())
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, homeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, homeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, lookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, searchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, lockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniPlayer(song: Song){
        binding.mainSongTitleTv.text = song.title
        binding.mainSongSingerTv.text = song.singer
        binding.songProgressbarView.progress = (song.second*100000)/song.playTime
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if(songs.isEmpty()) {
            songDB.songDao().insert(
                Song(
                    "LiLac",
                    "아이유 (IU)",
                    0,
                    200,
                    false,
                    "music_lilac",
                    R.drawable.img_album_exp2,
                    false
                )
            )

            songDB.songDao().insert(
                Song(
                    "FLu",
                    "아이유 (IU)",
                    0,
                    200,
                    false,
                    "music_lilac",
                    R.drawable.img_album_exp2,
                    false
                )
            )
            val _songs = songDB.songDao().getSongs()
            Log.d("DB data", _songs.toString())
        }
    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.AlbumDao().getAlbums()

        if(albums.isEmpty()) {
            songDB.AlbumDao().insert(
                Album(
                    0,
                    "IU 5th Album 'LILAC",
                    "아이유(IU)",
                    R.drawable.img_album_exp2
                )
            )

            songDB.AlbumDao().insert(
                Album(
                    1,
                    "Butter",
                    "방탄소년단 (BTS)",
                    R.drawable.img_album_exp
                )
            )
            val _albums = songDB.AlbumDao().getAlbums()
            Log.d("DB data", _albums.toString())
        }
    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt", "")
    }

     override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("song", null)
//        song = if(songJson == null){
//            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
//        }else{
//            gson.fromJson(songJson, Song::class.java) //songJson의 Json형식을 Song클래스 자바 객체로 변환
//        }

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0){
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }
        setMiniPlayer(song)
    }
}