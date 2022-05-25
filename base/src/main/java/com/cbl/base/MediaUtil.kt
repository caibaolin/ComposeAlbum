package com.cbl.base

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.MediaStore
import com.cbl.base.bean.AlbumBean
import com.cbl.base.bean.AlbumData
import com.cbl.base.bean.MediaBean
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


object MediaUtil {
    val start = AtomicBoolean(false)
    val albumData = MutableStateFlow(AlbumData())

    /**
     * 需要从数据库中获取的信息：
     * BUCKET_DISPLAY_NAME  文件夹名称
     * DATA  文件路径
     */

    @SuppressLint("Range")
    suspend fun getData() {
        start.set(true)
        Timber.i(
            "getData start thread-->${Thread.currentThread().name}  Images uri-->${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}" +
                    "  video uri-->${MediaStore.Video.Media.EXTERNAL_CONTENT_URI}"
        )
        val albumList = mutableListOf<AlbumBean>()
        val albumMap = mutableMapOf<String, AlbumBean>()

        var cursor: Cursor? = BaseApp.CONTEXT.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  //限制类型为图片
            arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.DATA

            ),
            null,
            null,  // 这里筛选了jpg和png格式的图片
            MediaStore.Images.Media.DATE_ADDED
        ) // 排序方式：按添加时间排序
        Timber.i("getData  thread-->${Thread.currentThread().name}  count-->${cursor?.count}")
        cursor?.let {
            while (it.moveToNext()) {
                val displayName =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val relativePath =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH))
                val path =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.DATA))
//                        Timber.i("displayName-->$displayName path-->$path")
                if (albumMap.containsKey(relativePath)) {
                    albumMap[relativePath]?.list?.add(MediaBean(path))
                } else {
                    val albumBean = AlbumBean(name = displayName, relativePath = relativePath)
                    albumBean.list.add(MediaBean(path))
                    albumMap[relativePath] = albumBean
                }
            }
            it.close()
        }
        var cursor2: Cursor? = BaseApp.CONTEXT.getContentResolver().query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,  //限制类型为图片
            arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.DATA

            ),
            null,
            null,  // 这里筛选了jpg和png格式的图片
            MediaStore.Images.Media.DATE_ADDED
        ) // 排序方式：按添加时间排序
        Timber.i("getData  thread-->${Thread.currentThread().name}  cursor2-->${cursor2?.count}")
        cursor2?.let {
            while (it.moveToNext()) {
                val displayName =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val relativePath =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH))
                val path =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.DATA))
//                        Timber.i("displayName-->$displayName path-->$path")
                if (albumMap.containsKey(relativePath)) {
                    albumMap[relativePath]?.list?.add(MediaBean(path))
                } else {
                    val albumBean = AlbumBean(name = displayName, relativePath = relativePath)
                    albumBean.list.add(MediaBean(path))
                    albumMap[relativePath] = albumBean
                }
            }
            it.close()
        }
        albumList.addAll(albumMap.values)
        albumData.emit(AlbumData(albumList))
        Timber.i("getData over thread-->${Thread.currentThread().name} albumMap-->${albumMap} size-->${albumMap.keys.size}")
        start.set(false)

    }


}