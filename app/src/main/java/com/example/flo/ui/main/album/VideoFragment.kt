package com.example.flo.ui.main.album

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    lateinit var binding: FragmentVideoBinding
    var items = arrayOf("최신순", "조회순")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        var adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, items)
        binding.videoSpinner.adapter = adapter
        return binding.root
    }
}