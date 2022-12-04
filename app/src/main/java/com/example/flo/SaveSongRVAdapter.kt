package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSavesongBinding

class SaveSongRVAdapter(private val savesongList: ArrayList<Album>): RecyclerView.Adapter<SaveSongRVAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSavesongBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album){
            binding.itemSavesongIv.setImageResource(album.coverImg!!)
            binding.itemSavesongTitleTv.text = album.title.toString()
            binding.itemSavesongSingerTv.text = album.singer.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavesongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savesongList[position])
    }

    override fun getItemCount(): Int = savesongList.size
}