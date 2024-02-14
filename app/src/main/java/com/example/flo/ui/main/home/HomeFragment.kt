package com.example.flo.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.data.remote.AlbumChar
import com.example.flo.data.remote.AlbumCharResult
import com.example.flo.data.remote.AlbumService
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.main.album.AlbumFragment
import com.example.flo.ui.main.album.AlbumRVAdapter
import com.example.flo.ui.main.look.SongRVAdapter
import com.google.gson.Gson

class HomeFragment : Fragment(), AlbumView {

    var currentPosition = 0
    lateinit var binding: FragmentHomeBinding
    val handler = Handler(Looper.getMainLooper()){
        setPage()
        true
    }
    var albumDatas: ArrayList<Album> = ArrayList()
    lateinit var songDB: SongDatabase
    lateinit var popcastAdapter: PopCastRVAdapter
    lateinit var albumRVAdapter: AlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        albumDatas = inputDummyAlbums()



        getAlbums()

        val bannerAdapter = BannerVPAdapter(this)
        val backgroundAdapter = HomeVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        backgroundAdapter.addFragment(HomeSubFragment(R.drawable.img_first_album_default))
        backgroundAdapter.addFragment(HomeSubFragment(R.drawable.img_album_exp4))
        backgroundAdapter.addFragment(HomeSubFragment(R.drawable.img_album_exp5))
        backgroundAdapter.addFragment(HomeSubFragment(R.drawable.img_album_exp6))
        backgroundAdapter.addFragment(HomeSubFragment(R.drawable.img_album_exp3))
        binding.homePopAcousticBanner.adapter = bannerAdapter
        binding.homePopAcousticBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homePannelBackgroundIv.adapter = backgroundAdapter
        binding.homePannelBackgroundIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicator.setViewPager(binding.homePannelBackgroundIv)
        val thread=Thread(PagerRunnable())
        thread.start()
        return binding.root
    }

    private fun changeAlbumFragment(result: AlbumChar) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(result)
                    putString("album", albumJson)
                }
            }).commitAllowingStateLoss()
    }

    fun setPage(){
        if(currentPosition==5) currentPosition=0
        binding.homePannelBackgroundIv.setCurrentItem(currentPosition,true)
        currentPosition+=1
    }

    inner class PagerRunnable: Runnable {
        override fun run() {
            while(true){
                Thread.sleep(2000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    private fun inputDummyAlbums(): ArrayList<Album>{
        songDB = SongDatabase.getInstance(requireContext())!!
        var datas = songDB.albumDao().getAlbums()
        if(!datas.isEmpty()) return (datas as ArrayList<Album>)
        songDB.albumDao().apply {
            insert(Album(  1,"Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            insert(Album(2, "IU 5th Album", "아이유 (IU)", R.drawable.img_album_exp2))
            insert(Album(3, "Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
            insert(Album(1, "Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            insert(Album(4, "BBooom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
            insert(Album(5, "Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
        }
        datas = songDB.albumDao().getAlbums()
        return (datas as ArrayList<Album>)
    }

    private fun getAlbums(){
        val albumService = AlbumService()
        albumService.setAlbumView(this)
        albumService.getAlbums()
    }

    private fun initAlbumRecyclerView(result: AlbumCharResult){
        val albumRVAdapter = AlbumRVAdapter(requireContext(),result)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        albumRVAdapter.setMyItemClickLister(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(result: AlbumChar) {
                changeAlbumFragment(result)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.deleteItem(position)
            }

            override fun playAlbum(album: Album) {
                songDB = SongDatabase.getInstance(requireContext())!!
                var playList = songDB.songDao().getAlbumSongs(album.id)
                (activity as MainActivity).releaseSong()
                (activity as MainActivity).initAlbumSongs(playList as ArrayList<Song>)
                (activity as MainActivity).setPlayerStatus(true)
            }
        })
    }

    private fun initPopCastRecyclerView(result: AlbumCharResult){
        popcastAdapter = PopCastRVAdapter(requireContext(), result)
        binding.homePopcastRv.adapter = popcastAdapter
        binding.homePopcastRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onAlbumListSuccess(result: AlbumCharResult) {
        initAlbumRecyclerView(result)
        initPopCastRecyclerView(result)
    }

    override fun onAlbumListFailure() {

    }
}