package com.example.flo.ui.main.locker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ItemSongBinding

class SaveRVAdapter(var context: Context): RecyclerView.Adapter<SaveRVAdapter.ViewHolder>() {

    private val songDatas: ArrayList<Song> = ArrayList()
    var check: Boolean = false

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemCLickListener: MyItemClickListener){
        mItemClickListener = itemCLickListener
    }

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.songTitle.text = song.title
            binding.songSinger.text = song.singer
            binding.songCoverImg.setImageResource(song.coverImg!!)
            if(song.isPlaying){
                binding.songPause.visibility = View.VISIBLE
                binding.songPlay.visibility = View.GONE
            }else{
                binding.songPause.visibility = View.GONE
                binding.songPlay.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songDatas[position])
        holder.binding.songInfo.setOnClickListener {
            mItemClickListener.onRemoveSong(songDatas[position].id)
            removeSong(position)
        }
        holder.binding.songPlay.setOnClickListener {
            songDatas[position].isPlaying = true
            holder.binding.songPause.visibility = View.VISIBLE
            holder.binding.songPlay.visibility = View.GONE
        }
        holder.binding.songPause.setOnClickListener {
            songDatas[position].isPlaying = false
            holder.binding.songPause.visibility = View.GONE
            holder.binding.songPlay.visibility = View.VISIBLE
        }
        if(check){
            holder.binding.songPlay.setBackgroundColor(context.resources.getColor(R.color.light_gray))
            holder.binding.songInfo.setBackgroundColor(context.resources.getColor(R.color.light_gray))
        }else{
            holder.binding.songPlay.setBackgroundColor(Color.WHITE)
            holder.binding.songInfo.setBackgroundColor(Color.WHITE)
        }
    }

    fun removeSong(position: Int){
        songDatas.removeAt(position)
        notifyDataSetChanged()
    }

    fun addSongs(songs: List<Song>){
        songDatas.clear()
        songDatas.addAll(songs)
        notifyDataSetChanged()
    }

    fun removeAllSongs(){
        for(i in songDatas){
            mItemClickListener.onRemoveSong(i.id)
        }
        songDatas.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = songDatas.size

}