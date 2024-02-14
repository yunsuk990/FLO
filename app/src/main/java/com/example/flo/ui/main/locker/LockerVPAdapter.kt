package com.example.flo.ui.main.locker
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SaveFragment()
            1-> FileFragment()
            else -> SavedAlbumFragment()
        }
    }
}