package com.example.flo.ui.main.album

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.entities.Album
import com.example.flo.data.remote.AlbumChar
import com.example.flo.data.remote.AlbumCharResult
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(val context: Context, val result: AlbumCharResult): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(result: AlbumChar)
        fun onRemoveAlbum(position: Int)

        fun playAlbum(album: Album)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickLister(itemClickLister: MyItemClickListener){
        mItemClickListener = itemClickLister
    }

    fun deleteItem(position: Int){
        result.albums.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
//        fun bind(album: Album){
//            binding.itemAlbumTitleTv.text = album.title
//            binding.itemAblumSingerTv.text = album.singer
//            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
//        }
        val title: TextView = binding.itemAlbumTitleTv
        val singer: TextView = binding.itemAblumSingerTv
        val coverImg: ImageView = binding.itemAlbumCoverImgIv
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(albumList[position])
        if(result.albums[position].coverImgUrl == "" || result.albums[position].coverImgUrl == null){

        }else{
            Log.d("image",result.albums[position].coverImgUrl)
            Glide.with(context).load(result.albums[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = result.albums[position].title
        holder.singer.text = result.albums[position].singer



        holder.itemView.setOnClickListener{
//            mItemClickListener.onItemClick(albumList[position])
            mItemClickListener.onItemClick(result.albums[position])
        }
//        holder.binding.itemAlbumTitleTv.setOnClickListener {
//            mItemClickListener.onRemoveAlbum(position)
//        }

        holder.binding.albumPlayIv.setOnClickListener {
//            mItemClickListener.playAlbum(albumList[position])
        }
    }

    override fun getItemCount(): Int = result.albums.size


}