package com.cbl.base.bean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/25 17:30
 *     desc   :
 * </pre>
 */
const val relative_path_camera = "DCIM/Camera/"
const val relative_path_screenshots = "Pictures/Screenshots/"
const val relative_path_root = "/"
const val relative_RECYCLER_IMG_DB = "RECYCLER_IMG_DB"
val cameraAlbumBean = AlbumBean(displayName = "相机", relative_path = relative_path_camera)
val screenshotsAlbumBean = AlbumBean(displayName = "截屏", relative_path = relative_path_screenshots)
val rootAlbumBean = AlbumBean(displayName = "设备存储", relative_path = "/")

data class AlbumBean(
    val list: MutableList<MediaBean> = mutableListOf(),
    var displayName: String = "",
    var relative_path: String = "",
    /*
    * todo
    * */
    var date_modified: Long = 0
) {
    fun getShowName(): String {
        return when {
            displayName.isEmpty() -> "设备存储"
            relative_path == relative_path_camera -> "相机"
            relative_path == relative_path_screenshots -> "截屏"
            else -> displayName
        }
    }

    fun getSortKey(): Long {
        return when (relative_path) {
            relative_path_camera -> Long.MAX_VALUE
            relative_path_screenshots -> Long.MAX_VALUE - 1
            relative_path_root -> Long.MAX_VALUE - 2
            relative_RECYCLER_IMG_DB -> Long.MIN_VALUE
            else -> date_modified
        }
    }
}
