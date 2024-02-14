package com.example.flo.ui.main.locker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment : Fragment() {

    lateinit var binding: FragmentSavedAlbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        albumDB = SongDatabase.getInstance(requireContext())!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.savedAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val albumRVAdapter = SavedLockerRVAdapter(albumDB.albumDao().getLikedAlbum(getJwt()) as ArrayList<Album>)
        albumRVAdapter.setMyItemClickLister(object: SavedLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(albumId: Int) {
                albumDB.albumDao().disLikedAlbum(getJwt(), albumId)
            }
        })
        binding.savedAlbumRv.adapter = albumRVAdapter

//        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbum(getJwt()) as ArrayList<Album>)
        Log.d("LikedAlbum", albumDB.albumDao().getLikedAlbum(getJwt()).toString())

    }
    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("userIdx", 0)
    }
}