package com.example.flo.ui.main.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.remote.AlbumResult
import com.example.flo.databinding.ItemAlbumSongBinding

class AlbumSongRVAdapter(val result: ArrayList<AlbumResult>): RecyclerView.Adapter<AlbumSongRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAlbumSongBinding): RecyclerView.ViewHolder(binding.root){
        val idx: TextView = binding.linear1Tv1
        val title: TextView = binding.linear1Tv3
        val singer: TextView = binding.linear1Tv4
        val titleBool: TextView = binding.linear1Tv2
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AlbumSongRVAdapter.ViewHolder {
        val binding = ItemAlbumSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumSongRVAdapter.ViewHolder, position: Int) {
        holder.idx.text = (position+1).toString()
        holder.title.text = result[position].title
        holder.singer.text = result[position].singer
        if(result[position].isTitleSong.equals("T")){
            holder.titleBool.visibility = View.VISIBLE
        }else{
            holder.titleBool.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = result.size


}