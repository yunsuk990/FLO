package com.example.flo.ui.main.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Like
import com.example.flo.data.remote.AlbumChar
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보","영상")
    private var gson: Gson = Gson()
    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
//        val album = gson.fromJson(albumJson, Album::class.java)
        val album = gson.fromJson(albumJson, AlbumChar::class.java)
//        isLiked = isLikedAlbum(album.id)
//        setInit(album)
//        setOnClickListeners(album)
        isLiked = isLikedAlbum(album.albumIdx)
        setInit(album)
        setOnClickListeners(album)


        binding.albumBackIb.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        }

        val albumAdapter = AlbumVPAdapter(this, album.albumIdx, album.singer)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumMenuTl, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()
        return binding.root
    }

//    private fun setInit(album: Album){
//        binding.albumTitleTv.text = album.title
//        binding.albumSingerTv.text = album.singer
//        binding.albumTitleImageIv.setImageResource(album.coverImg!!)
//        if(isLiked){
//            binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_on)
//        }else{
//            binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_off)
//        }
//    }
    private fun setInit(album: AlbumChar){
        binding.albumTitleTv.text = album.title
        binding.albumSingerTv.text = album.singer
        Glide.with(requireContext()).load(album.coverImgUrl!!).into(binding.albumTitleImageIv)
        if(isLiked){
            binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_off)
        }
    }
    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("userIdx", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)
        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()
        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)
        return likeId != null
    }

    private fun disLikedAlbum(albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()
        songDB.albumDao().disLikedAlbum(userId, albumId)
    }

//    private fun setOnClickListeners(album: Album){
//        val userId = getJwt()
//        binding.albumHeartIb.setOnClickListener{
//            if(isLiked){
//                binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_off)
//                disLikedAlbum(album.id)
//            }else{
//                binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_on)
//                likeAlbum(userId,album.id)
//            }
//            isLiked = !isLiked
//        }
//    }
    private fun setOnClickListeners(album: AlbumChar){
        val userId = getJwt()
        binding.albumHeartIb.setOnClickListener{
            if(isLiked){
                binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.albumIdx)
            }else{
                binding.albumHeartIb.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.albumIdx)
            }
            isLiked = !isLiked
        }
    }
}