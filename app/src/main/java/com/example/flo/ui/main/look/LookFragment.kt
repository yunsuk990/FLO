package com.example.flo.ui.main.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.remote.FloCharResult
import com.example.flo.data.remote.SongService
import com.example.flo.databinding.FragmentLookBinding

class LookFragment : Fragment(), LookView {

    lateinit var binding: FragmentLookBinding
    private lateinit var floCharAdapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun initRecyclerView(result: FloCharResult){
        floCharAdapter = SongRVAdapter(requireContext(), result)
        Log.d("init",floCharAdapter.result.toString())
        binding.lookFloChartRv.adapter = floCharAdapter
        binding.lookFloChartRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getSongs(){
        val songService = SongService()
        songService.setLookView(this)
        songService.getSongs()
    }

    override fun onGetSongLoading() {
        binding.lookProgressPb.bringToFront()
        binding.lookProgressPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloCharResult) {
        binding.lookProgressPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookProgressPb.visibility = View.INVISIBLE
    }
}