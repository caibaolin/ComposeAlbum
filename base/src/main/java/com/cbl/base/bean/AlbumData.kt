package com.cbl.base.bean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/25 17:31
 *     desc   :
 * </pre>
 */
data class AlbumData(
    val alllist: MutableList<AlbumBean> = mutableListOf(),
    val imageAlbums: MutableMap<String, AlbumBean> = mutableMapOf(),
    val imageAlbumsNoGif: MutableMap<String, AlbumBean> = mutableMapOf(),
    val videoAlbums: MutableMap<String, AlbumBean> = mutableMapOf(),
    val greenList: List<String> = listOf(),
    val dbAlbumBean: AlbumBean = AlbumBean()
)
/*
data class AlbumData(
    val alllist: MutableList<AlbumBean> = mutableListOf(),
)*/
