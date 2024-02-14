package com.example.flo.ui.main.look

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.FloCharResult
import com.example.flo.databinding.ItemLookSongBinding
import com.example.flo.databinding.ItemSongBinding
import org.w3c.dom.Text

class SongRVAdapter(val context: Context, val result: FloCharResult) :RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemLookSongBinding): RecyclerView.ViewHolder(binding.root){
        val coverImg: ImageView = binding.songCoverImg
        val title: TextView = binding.songTitle
        val singer: TextView = binding.songSinger
        val songId: TextView = binding.songIdTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLookSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(result.songs[position].coverImgUrl == "" || result.songs[position].coverImgUrl == null){

        }else{
            Log.d("image",result.songs[position].coverImgUrl)
            Glide.with(context).load(result.songs[position].coverImgUrl).into(holder.coverImg)
        }
        holder.songId.text = (position+1).toString()
        holder.title.text = result.songs[position].title
        holder.singer.text = result.songs[position].singer
    }

    override fun getItemCount(): Int = result.songs.size

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


}