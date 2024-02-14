package com.example.flo.ui.main.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.R
import com.example.flo.data.remote.AlbumResult
import com.example.flo.data.remote.AlbumService
import com.example.flo.databinding.FragmentSongBinding

class SongFragment(val albumIdx: Int) : Fragment(), AlbumSongView {

    lateinit var binding: FragmentSongBinding
    var toggle: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.albumSwitch.setOnClickListener{
            toggle = !toggle
            if(toggle){
                binding.albumSwitchIv.setImageResource(R.drawable.btn_toggle_on)
            }else{
                binding.albumSwitchIv.setImageResource(R.drawable.btn_toggle_off)
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getAlbumSongs()
    }

    private fun getAlbumSongs(){
        val albumService = AlbumService()
        albumService.setAlbumSongView(this)
        albumService.getAlbumSongs(albumIdx)
    }

    private fun initRecyclerView(result: ArrayList<AlbumResult>){
        val albumSongAdapter = AlbumSongRVAdapter(result)
        binding.songRv.adapter = albumSongAdapter
        binding.songRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onAlbumSongSuccess(result: ArrayList<AlbumResult>) {
        initRecyclerView(result)
        binding.songProgressPb.visibility = View.INVISIBLE
    }

    override fun onAlbumSongFailure() {

    }
    override fun onAlbumSongLoading() {
        binding.songProgressPb.visibility = View.VISIBLE
    }


}