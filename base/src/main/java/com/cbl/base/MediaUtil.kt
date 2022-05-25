package com.cbl.base

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.MediaStore
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


object MediaUtil {
    val start = AtomicBoolean(false)

    /**
     * 需要从数据库中获取的信息：
     * BUCKET_DISPLAY_NAME  文件夹名称
     * DATA  文件路径
     */
    private val projection = arrayOf(
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA
    )

    @SuppressLint("Range")
    suspend fun getData() {
        start.set(true)
        Timber.i("getData start thread-->${Thread.currentThread().name}")
        /**
         * 通过ContentResolver 从媒体数据库中读取图片信息
         */
        var cursor: Cursor? = BaseApp.CONTEXT.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  //限制类型为图片
            projection,
            null,
            null,  // 这里筛选了jpg和png格式的图片
            MediaStore.Images.Media.DATE_ADDED
        ) // 排序方式：按添加时间排序
        Timber.i("getData  thread-->${Thread.currentThread().name}  count-->${cursor?.count}")
        var i = 0
        cursor?.let {
            while (cursor.moveToNext()) {
                val displayName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val path =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
//                        Timber.i("displayName-->$displayName path-->$path")
                i++
            }
        }
        Timber.i("getData over thread-->${Thread.currentThread().name} i-->$i")
        start.set(false)

    }


}