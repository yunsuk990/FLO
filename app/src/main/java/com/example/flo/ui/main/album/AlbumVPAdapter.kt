package com.example.flo.ui.main.album
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment: Fragment, val albumIdx: Int, val singer: String): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment(albumIdx)
            1 -> DetailFragment(singer)
            else -> VideoFragment()
        }
    }
}