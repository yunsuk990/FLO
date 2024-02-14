package com.example.flo.ui.main.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.data.remote.LastFmService
import com.example.flo.databinding.FragmentDetailBinding

class DetailFragment(val singer: String) : Fragment() {

    lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val lastFmService = LastFmService()
        lastFmService.getArtistInfo(requireContext(),singer)
    }
}