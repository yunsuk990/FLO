package com.example.flo.ui.main

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.locker.LockerFragment
import com.example.flo.ui.main.look.LookFragment
import com.example.flo.R
import com.example.flo.ui.main.search.SearchFragment
import com.example.flo.ui.song.SongActivity
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var gson: Gson = Gson()
    val songs = arrayListOf<Song>()
    var nowPos = 0
    lateinit var songDB: SongDatabase
    private var mediaPlayer: MediaPlayer? = null
    var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.mainPlayerCl.setOnClickListener{
            releaseSong()
            val intent = Intent(this, SongActivity::class.java)
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()
            startActivityForResult(intent, 0)
        }

        binding.mainNextBtn.setOnClickListener{
            moveSong(+1)
        }

        binding.mainPreviousBtn.setOnClickListener{
            moveSong(-1)
        }

        binding.mainMiniplayerBtn.setOnClickListener{
            setPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener{
            setPlayerStatus(false)
        }



        initBottomNavigation()
        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())

    }

    private fun moveSong(direction: Int){
        if(nowPos + direction < 0){
            Toast.makeText(this, "첫 곡입니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direction >= songs.size){
            Toast.makeText(this, "마지막 곡입니다.",Toast.LENGTH_SHORT).show()
            return
        }
        songs[nowPos].second = 0
//        saveSong(songs[nowPos])
        releaseSong()
        nowPos += direction
        setMiniPlayer(songs[nowPos])
        Log.d("nextSong", songs[nowPos].toString())
        setPlayerStatus(true)

    }


    private fun saveSong(song : Song){
        songDB = SongDatabase.getInstance(this)!!
        songDB.songDao().update(song)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, data?.getStringExtra("title"), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()


        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun setMiniPlayer(song: Song){
        binding.mainTitleTv.text = song.title
        binding.mainSingerTv.text = song.singer
        binding.mainSeekbar.progress = (song.second*100000)/song.playTime
        setPlayerStatus(false)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, music)
        }
        if(song.second != 0){
            mediaPlayer!!.seekTo(song.second * 1000 + 1200)
        }
        startTimer()
    }

    fun setPlayerStatus(isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer?.isPlaying = isPlaying
        if(isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }else{
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
//                serviceStop()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart()")
        //더미데이터 삽입
        songs.clear()
        inputDummySongs()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        if(songId != 0){
            songs.clear()
            songs.add(songDB.songDao().getSong(songId))
        }

        nowPos = getPlayingSongPosition(songId)
        Log.d("songId", songId.toString())
        Log.d("songs[nowPos]", songs[nowPos].toString())
        setMiniPlayer(songs[nowPos])
    }

    fun initAlbumSongs(playList: ArrayList<Song>){
        songs.clear()
        songs.addAll(playList)
        nowPos = 0
        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId:Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun inputDummySongs(){
        songDB = SongDatabase.getInstance(this)!!
//        songDB.albumDao().insert(Album(  1,"Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//        songDB.albumDao().insert(Album(2, "IU 5th Album", "아이유 (IU)", R.drawable.img_album_exp2))
//        songDB.albumDao().insert(Album(3, "Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//        songDB.albumDao().insert(Album(4, "BBooom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//        songDB.albumDao().insert(Album(5, "Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
        songs.addAll(songDB.songDao().getSongs())
        Log.d("songs", songs.toString())
        if(songs.isNotEmpty()) return
        songDB.songDao().insert(
            Song("Boy with Luv", "방탄소년단 (BTS)",0,60,false,"boywithluv",
                R.drawable.img_album_exp4, false,1)
        )
        songDB.songDao().insert(
            Song("Butter", "방탄소년단 (BTS)",0,60,false,"butter", R.drawable.img_album_exp, false, 1)
        )
        songDB.songDao().insert(
            Song("라일락", "아이유(IU)",0,60,false,"kidwine_repeat", R.drawable.img_album_exp2, false,2)
        )


        val song_DB = songDB.songDao().getSongs()
        Log.d("song_DB", song_DB.toString())
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer!!.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread(){
        private var second: Int = songs[nowPos].second
        private var mills: Float = (second * 1000).toFloat()

        override fun run() {
            super.run()
            try {
                while (true){
                    if (second >= playTime){
                        runOnUiThread{
                            second = 0
                            mills = 0f
                            binding.mainMiniplayerBtn.visibility = View.VISIBLE
                            binding.mainPauseBtn.visibility = View.GONE
                            //Toast.makeText(this@SongActivity, "스레드 종료", Toast.LENGTH_SHORT).show()
                            mediaPlayer?.seekTo(0)
                        }
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50
                        runOnUiThread{
                            binding.mainSeekbar.progress = ((mills/ playTime)*100).toInt()
                        }
                        if (mills % 1000 == 0f){
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.e("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }

    fun releaseSong(){
        timer?.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "OnDestroy()")
        timer?.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        songs[nowPos].second = ((binding.mainSeekbar.progress * songs[nowPos].playTime)/100)/1000

        //현재 상태 DB에 저장
        songDB.songDao().update(songs[nowPos])

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    private fun getJwt(): String?{
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        return spf!!.getString("jwt", "")
    }
}