package com.example.flo.ui.main.locker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.databinding.FragmentFileBinding
import com.example.flo.databinding.FragmentSaveBinding
import com.example.flo.databinding.FragmentVideoBinding

class FileFragment : Fragment() {
    lateinit var binding: FragmentFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(inflater, container, false)
        return binding.root
    }

}