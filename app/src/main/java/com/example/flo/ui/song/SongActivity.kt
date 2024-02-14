package com.example.flo.ui.song

import android.content.Intent
import android.graphics.Point
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.flo.Foreground
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.ui.main.MainActivity
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    var playAgain: Int = 1
    var playAll: Boolean = false
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SongActivity", "onCreate()")
        songDB = SongDatabase.getInstance(this)!!
        Log.d("SongActivity_songs", songDB.songDao().getSongs().toString())
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onStart() {
        super.onStart()
        songs.clear()
        Log.d("SongActivity", "onStart()")
        initClickListener()
        initPlayList()
    }

    //DB에서 모든 곡 가져와 songs리스트 초기화
    private fun initPlayList(){
        songs.addAll(songDB.songDao().getSongs()).apply {
            initSong()
        }
    }

    //사용할 곡 가져오기
    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        nowPos = getPlayingSongPosition(songId)
        startTimer()
        setPlayer(songs[nowPos])
    }


    //화면 곡 정보들 초기화
    private fun setPlayer(song: Song){
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime%60)
        binding.songPlayerPb.progress = (song.second * 1000 / song.playTime) * 100
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        Log.d("songIsLike", song.isLike.toString())

        //좋아요 버튼 초기화
        if(song.isLike){
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_off)
        }


        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, music)
        }
        if(song.second != 0){
            mediaPlayer!!.seekTo(song.second * 1000 + 1500)
        }
        setPlayerStatus(song.isPlaying)


    }

    //songs리스트 속 현재 재생하는 곡의 위치(index) 가져오기
    private fun  getPlayingSongPosition(songId:Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }


    //곡의 재생, 정지에 따라 UI 변경
    fun setPlayerStatus(isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying){
            binding.songPlayIb.visibility = View.GONE
            binding.songPauseIb.visibility = View.VISIBLE
            mediaPlayer?.start()
        }else{
            binding.songPlayIb.visibility = View.VISIBLE
            binding.songPauseIb.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
//                serviceStop()
            }
        }
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this, "첫 곡입니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this, "마지막 곡입니다.",Toast.LENGTH_SHORT).show()
            return
        }

        //새로운 곡 재생

        nowPos += direct
        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])

    }

    //클릭 이벤트들 모음
    private fun initClickListener(){
        binding.songDownIb.setOnClickListener{
            var intent: Intent = Intent(this, MainActivity::class.java)
            intent.putExtra("title", binding.songTitleTv.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.songPlayIb.setOnClickListener{
            serviceStart(songs[nowPos])
            setPlayerStatus(true)
        }

        binding.songPauseIb.setOnClickListener{
            setPlayerStatus(false)
        }

        binding.songPlayStyleIb.setOnClickListener {
            playAgain++
            playAgain = playAgain % 3
            if(playAgain == 1){
                binding.songPlayStyleIb.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            }else if (playAgain == 2){
                binding.songPlayStyleIb.setImageResource(R.drawable.btn_player_repeat_on_light)
            }else{
                binding.songPlayStyleIb.setImageResource(R.drawable.btn_player_repeat_on1_light)
            }

        }

        binding.songRandomIb.setOnClickListener {
            playAll = !playAll
            if(playAll){
                binding.songRandomIb.setImageResource(R.drawable.btn_playlist_random_on)
            }else{
                binding.songRandomIb.setImageResource(R.drawable.nugu_btn_random_inactive)

            }
        }

        binding.songNextIb.setOnClickListener{
            moveSong(+1)
        }

        binding.songPreviousIb.setOnClickListener{
            moveSong(-1)
        }

        binding.songLikeIb.setOnClickListener{
            setLike(songs[nowPos].isLike)
        }
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if(!isLike){
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_on)
            customToastView("좋아요 한 곡에 담겼습니다.")
        }else{
            binding.songLikeIb.setImageResource(R.drawable.ic_my_like_off)
            customToastView("좋아요 한 곡에서 삭제하였습니다.")
        }
    }

    //타이머 시작
    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    //타이머 정의
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
                            binding.songPlayIb.visibility = View.VISIBLE
                            binding.songPauseIb.visibility = View.GONE
                            //Toast.makeText(this@SongActivity, "스레드 종료", Toast.LENGTH_SHORT).show()
                            binding.songStartTimeTv.text = String.format("%02d:%02d", 0,0)
                            mediaPlayer?.seekTo(0)
                            if(playAgain == 0){
                                setPlayerStatus(true)
                            }else{
                                setPlayerStatus(false)

                            }
                        }
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50
                        runOnUiThread{
                            binding.songPlayerPb.progress = ((mills/ playTime)*100).toInt()
                        }
                        if (mills % 1000 == 0f){
                            runOnUiThread{
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60,second%60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.e("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }

    //현재 재생 곡 ID 저장
    override fun onPause() {
        super.onPause()
        Log.d("SongActivity", "onPause()")
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songPlayerPb.progress * songs[nowPos].playTime)/100)/1000

        //현재 상태 DB에 저장
        Log.d("songs_Pause", songs[nowPos].toString())
        songDB.songDao().update(songs[nowPos])

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("SongActivity", "onDestroy()")
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun serviceStart(song: Song){
        val intent = Intent(this, Foreground::class.java)
        intent.putExtra("song", gson.toJson(song))
        ContextCompat.startForegroundService(this, intent)
    }

    fun serviceStop(){
        val intent = Intent(this, Foreground::class.java)
        stopService(intent)
    }

    fun customToastView(text: String){
        var inflater: LayoutInflater = layoutInflater
        var layout : View = inflater.inflate(R.layout.toast_board, findViewById(R.id.toast_root))
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val height = size.y

        var textView = layout.findViewById(R.id.toast_text) as TextView
        textView.setText(text)
        var toastView = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toastView.setGravity(Gravity.FILL_HORIZONTAL,0, (height*0.22).toInt())
        toastView.view = layout
        toastView.show()
    }
}