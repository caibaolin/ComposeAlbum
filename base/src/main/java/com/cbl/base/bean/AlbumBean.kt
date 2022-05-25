package com.cbl.base.bean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/25 17:30
 *     desc   :
 * </pre>
 */
data class AlbumBean(val list: MutableList<MediaBean> = mutableListOf(), val name:String, val relativePath:String)
