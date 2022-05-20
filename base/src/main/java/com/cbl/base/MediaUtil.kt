package com.cbl.base

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.MediaStore
import timber.log.Timber


object MediaUtil {
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
    fun getData() {
        /**
         * 通过ContentResolver 从媒体数据库中读取图片信息
         */
        var cursor: Cursor? = BaseApp.CONTEXT.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  //限制类型为图片
            projection,
            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
            arrayOf("image/jpeg", "image/png"),  // 这里筛选了jpg和png格式的图片
            MediaStore.Images.Media.DATE_ADDED
        ) // 排序方式：按添加时间排序
        cursor?.let {
            while (cursor.moveToNext()) {
                val displayName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                Timber.i("displayName-->$displayName path-->$path")
            }
        }
    }


}