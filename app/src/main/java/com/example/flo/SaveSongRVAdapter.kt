package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.Database.Song
import com.example.flo.databinding.ItemSavesongBinding

class SaveSongRVAdapter(): RecyclerView.Adapter<SaveSongRVAdapter.ViewHolder>() {

    private val songs: ArrayList<Song> = ArrayList<Song>()

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemSavesongBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song){
            binding.itemSavesongIv.setImageResource(song.coverImg!!)
            binding.itemSavesongTitleTv.text = song.title
            binding.itemSavesongSingerTv.text = song.singer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavesongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.btnPlayerMore.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            deleteSong(position)

        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>){
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

}