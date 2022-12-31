package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.Database.Song
import com.example.flo.Database.SongDatabase
import com.example.flo.databinding.FragmentSaveSongBinding

class SaveSongFragment: Fragment()  {

    lateinit var binding: FragmentSaveSongBinding
    lateinit var songDB: SongDatabase
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val songRVAdapter = SaveSongRVAdapter()
        songRVAdapter.setMyMyItemClickListener(object : SaveSongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })
        binding.savesongRV.adapter = songRVAdapter
        binding.savesongRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
}