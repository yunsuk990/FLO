package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity: AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    var checkRepeat: Boolean = false
    var checkRandom: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //Intent에 값이 있는지 확인
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }
    }

    fun setPlayerStatus(isPlaying: Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }




}