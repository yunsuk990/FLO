package com.example.flo

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity: AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    var checkRepeat: Boolean = false
    var checkRandom: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding .inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener{
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(true)
        }

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

    private fun initSong(){
        //Intent에 값이 있는지 확인
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0)!!,
                intent.getIntExtra("playTime", 0)!!,
                intent.getBooleanExtra("isPlaying", false)!!,
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("singer")
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60 )
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60 )
        binding.songProgressbarView.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music) //음악 재생
        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            mediaPlayer?.start()
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.stop()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
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




}