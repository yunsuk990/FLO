package com.example.flo.ui.main.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.AlbumCharResult
import com.example.flo.databinding.ItemAlbumBinding

class PopCastRVAdapter(val context: Context, val result: AlbumCharResult): RecyclerView.Adapter<PopCastRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        val coverImg: ImageView = binding.itemAlbumCoverImgIv
        val title = binding.itemAlbumTitleTv
        val singer = binding.itemAblumSingerTv
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopCastRVAdapter.ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopCastRVAdapter.ViewHolder, position: Int) {
        if(result.albums[position].coverImgUrl == "" || result.albums[position].coverImgUrl == null){

        }else{
            Log.d("image",result.albums[position].coverImgUrl)
            Glide.with(context).load(result.albums[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = result.albums[position].title
        holder.singer.text = result.albums[position].singer

    }

    override fun getItemCount(): Int = result.albums.size
}