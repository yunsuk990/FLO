package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment: Fragment() {

    lateinit var binding: FragmentAlbumBinding
    var checkAlbumIpIv = false
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        binding.albumLikeIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, homeFragment())
                .commitAllowingStateLoss()
        }

        binding.albumIpIv.setOnClickListener{
            if(checkAlbumIpIv){
                binding.albumIpIv.setImageResource(R.drawable.ic_my_like_off)
                checkAlbumIpIv = false

            }else{
                binding.albumIpIv.setImageResource(R.drawable.ic_my_like_on)
                checkAlbumIpIv = true
            }
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumTabLayout, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()


        return binding.root
    }




}