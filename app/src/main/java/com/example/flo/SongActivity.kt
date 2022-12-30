package com.example.flo

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.Database.Song
import com.example.flo.Database.SongDatabase
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity: AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    var checkRepeat: Boolean = false
    var checkRandom: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding .inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()

        binding.songRepeatIv.setOnClickListener {
            if(checkRepeat){
                binding.songRepeatIv.setColorFilter(Color.parseColor("#000000"))
                checkRepeat = false
            }else{
                binding.songRepeatIv.setColorFilter(Color.parseColor("#FFFF0000"))
                checkRepeat = true
            }
        }

        binding.songRandomIv.setOnClickListener {
            if (checkRandom){
                binding.songRandomIv.setColorFilter(Color.parseColor("#000000"))
                checkRandom = false
            }else{
                binding.songRandomIv.setColorFilter(Color.parseColor("#FFFF0000"))
                checkRandom = true
            }
        }
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener{
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        nowPos = getPlayingSongPosition(songId)
        Log.d("now SOng ID", songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct
        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer = null
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60 )
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60 )
        binding.songProgressbarView.progress = (song.second * 1000 / song.playTime)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music) //음악 재생
        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.stop()
            }

        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread(){
        private var second: Int = 0
        private var mills: Float = 0f
        override fun run() {
            super.run()
            while (true){
                if(second >= playTime){
                    break
                }
                if(isPlaying){
                    sleep(50)
                    mills += 50
                    runOnUiThread{
                        binding.songProgressbarView.progress = ((mills / playTime) * 100).toInt()
                    }
                    if (mills % 1000 == 0f){
                        runOnUiThread{
                            binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60 )
                        }
                        second ++
                    }
                }
            }
        }
    }

    //사용자가 포커스를 잃었을때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songProgressbarView.progress * songs[nowPos].playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        //Gson 사용 (라이브러리 추가)
        editor.putInt("song", songs[nowPos].id)
        editor.apply()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }
}