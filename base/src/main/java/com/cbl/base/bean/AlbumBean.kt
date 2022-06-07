package com.cbl.base.bean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/25 17:30
 *     desc   :
 * </pre>
 */
data class AlbumBean(
    val list: MutableList<MediaBean> = mutableListOf(),
    var displayName: String = "",
    var relative_path: String = ""
){
    fun getShowName():String{
        return if(displayName.isEmpty()){
            "设备存储"
        }else if (relative_path.equals("DCIM/Camera/")){
            "相机"
        }else if (relative_path.equals("Pictures/Screenshots/")){
            "截屏"
        }else{
            displayName
        }
    }
}
