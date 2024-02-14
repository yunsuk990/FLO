package com.example.flo.ui.main.locker

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.databinding.FragmentSaveBinding
import com.example.flo.ui.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class SaveFragment : Fragment() {
    lateinit var binding: FragmentSaveBinding
    lateinit var saveAdapter: SaveRVAdapter
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        initRecyclerView()

        binding.saveCheckAllLinear.setOnClickListener {
            setView(true)
            showBottomSheet()
        }



        return binding.root
    }

    private fun setView(check: Boolean){
        if(check){
            binding.saveCheckAllTv.text = "선택해제"
            binding.saveCheckAllTv.setTextColor(resources.getColor(R.color.select_color))
            binding.saveCheckAllIv.setImageResource(R.drawable.btn_playlist_select_on)
            binding.saveRv.setBackgroundColor(resources.getColor(R.color.light_gray))
            saveAdapter.check = true
            saveAdapter.notifyDataSetChanged()
        }else{
            binding.saveCheckAllTv.text = "전체선택"
            binding.saveCheckAllTv.setTextColor(Color.BLACK)
            binding.saveCheckAllIv.setImageResource(R.drawable.btn_playlist_select_off)
            binding.saveRv.setBackgroundColor(resources.getColor(R.color.white))
            saveAdapter.check = false
            saveAdapter.notifyDataSetChanged()
        }
    }
    private fun initRecyclerView(){
        saveAdapter = SaveRVAdapter(requireContext())
        binding.saveRv.adapter = saveAdapter
        binding.saveRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        saveAdapter.addSongs(songDB.songDao().getLikedSongs(true))
        saveAdapter.setMyItemClickListener(object: SaveRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
                (activity as MainActivity).songs.clear()
                (activity as MainActivity).songs.addAll(songDB.songDao().getSongs())
                Log.d("Save_onRemoveSong", songDB.songDao().getLikedSongs(true).toString())
            }
        })
    }

    private fun showBottomSheet(){
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetView.findViewById<LinearLayout>(R.id.bottom_sheet_listen_linear).setOnClickListener {
            setView(false)
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.bottom_sheet_playlist_linear).setOnClickListener {
            setView(false)
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<LinearLayout>(R.id.bottom_sheet_mylist_linear).setOnClickListener {
            setView(false)
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<ImageButton>(R.id.bottom_sheet_delete_linear).setOnClickListener {
            saveAdapter.removeAllSongs()
            setView(false)
            (activity as MainActivity).songs.clear()
            (activity as MainActivity).songs.addAll(songDB.songDao().getSongs())
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnCancelListener {
            setView(false)
        }


        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.window?.setGravity(Gravity.CENTER)
        bottomSheetDialog.show()
    }

}