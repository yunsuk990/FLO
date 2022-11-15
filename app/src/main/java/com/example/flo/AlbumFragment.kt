package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding

class AlbumFragment: Fragment() {

    lateinit var binding: FragmentAlbumBinding
    var checkAlbumIpIv = false

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
        return binding.root
    }




}