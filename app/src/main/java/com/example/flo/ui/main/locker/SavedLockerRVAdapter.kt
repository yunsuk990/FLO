package com.example.flo.ui.main.locker
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemLockerAlbumBinding

class SavedLockerRVAdapter( private val albums: ArrayList<Album>): RecyclerView.Adapter<SavedLockerRVAdapter.ViewHolder>() {

//    private val albums = ArrayList<Album>()

    interface MyItemClickListener {
        fun onRemoveSong(albumId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickLister(itemClickLister: MyItemClickListener){
        mItemClickListener = itemClickLister
    }


    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.lockerAlbumTitleTv.text = album.title
            binding.lockerAlbumSingerTv.text = album.singer
            binding.lockerCoverImgIv.setImageResource(album.coverImg!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = albums.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.binding.lockerAlbumMoreIv.setOnClickListener{
            mItemClickListener.onRemoveSong(albums[position].id)
            deleteAlbum(position)
        }
    }

    fun addAlbums(albums: ArrayList<Album>){
        Log.d("RV_albums", albums.toString())
        albums.clear()
        albums.addAll(albums)
        notifyDataSetChanged()
    }

    fun deleteAlbum(position: Int){
        albums.removeAt(position)
        notifyDataSetChanged()
    }




}