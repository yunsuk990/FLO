package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSongBinding

class SongRVAdapter(private val songList: ArrayList<Album>): RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener =  itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongRVAdapter.ViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = songList.size

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.itemSongNumberTv.text = (position + 1).toString()
            binding.itemSongIv.setImageResource(songList[position].coverImg!!)
            binding.itemSongTitleTv.text = songList[position].title
            binding.itemSongSingerTv.text = songList[position].singer
        }
    }
}