package com.example.flo.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.databinding.FragmentHomesubBinding

class HomeSubFragment(val imgRes: Int) : Fragment() {

    lateinit var binding: FragmentHomesubBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomesubBinding.inflate(inflater, container, false)
        binding.homeSubIv.setImageResource(imgRes)
        return binding.root
    }

}