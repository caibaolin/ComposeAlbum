package com.cbl.base

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.cbl.base.bean.AlbumBean
import com.cbl.base.bean.AlbumData
import com.cbl.base.bean.MediaBean
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


object MediaUtil {
    val albumData = MutableStateFlow(AlbumData())
    private val projection = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.DURATION,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.RELATIVE_PATH,
    )

    private fun getMediaBeanFromCursor(cursor: Cursor): MediaBean {
        val path = cursor.getString(0)
        val mimeType = cursor.getString(1)
        val date_modified = cursor.getLong(2)
        val date_added = cursor.getLong(3)
        val duration = cursor.getLong(4)
        val _size = cursor.getLong(5)
        val bucket_display_name = cursor.getString(6)
        val relative_path = cursor.getString(7)
        return MediaBean(
            path,
            mimeType,
            date_modified,
            date_added,
            duration,
            _size,
            bucket_display_name,
            relative_path
        )
    }

    suspend fun getData()=coroutineScope {
        val time=System.currentTimeMillis();
        val image_AlbumBeanTask = async { getData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI) }
        val video_AlbumBeanTask = async { getData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI) }
        val image_AlbumBean=image_AlbumBeanTask.await()
        val video_AlbumBean=video_AlbumBeanTask.await()
        val allAlbumBeanMap = mutableMapOf<String, AlbumBean>()
        allAlbumBeanMap.putAll(image_AlbumBean)
        video_AlbumBean.forEach {
            if (allAlbumBeanMap.containsKey(it.key)) {
                allAlbumBeanMap[it.key]?.list?.addAll(it.value.list)
            } else {
                allAlbumBeanMap[it.key] = it.value
            }
        }
        val mAlbumBeanList = mutableListOf<AlbumBean>()
        mAlbumBeanList.addAll(allAlbumBeanMap.values)
        albumData.emit(AlbumData(mAlbumBeanList))
        Timber.i("getData over-->${System.currentTimeMillis()-time}")
    }

    /**
     * 需要从数据库中获取的信息：
     * BUCKET_DISPLAY_NAME  文件夹名称
     * DATA  文件路径
     */
    private fun getData(uri: Uri): MutableMap<String, AlbumBean> {
        Timber.i(
            "getData start thread-->${Thread.currentThread().name}  Images uri-->${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}" +
                    "  video uri-->${MediaStore.Video.Media.EXTERNAL_CONTENT_URI} current uri-->${uri}"
        )
        val albumMap = mutableMapOf<String, AlbumBean>()

        var cursor: Cursor? = BaseApp.CONTEXT.getContentResolver().query(
            uri,  //限制类型为图片
            projection,
            null,
            null,  // 这里筛选了jpg和png格式的图片
            MediaStore.Images.Media.DATE_ADDED
        ) // 排序方式：按添加时间排序
        Timber.i("getData  thread-->${Thread.currentThread().name}  count-->${cursor?.count}")
        cursor?.let {
            while (it.moveToNext()) {
                val mediaBean = getMediaBeanFromCursor(it);
                Timber.i("displayName-->${mediaBean.bucket_display_name} path-->${mediaBean.path}")
                if (albumMap.containsKey(mediaBean.relative_path)) {
                    albumMap[mediaBean.relative_path]?.list?.add(mediaBean)
                } else {
                    val albumBean = AlbumBean(
                        name = mediaBean.bucket_display_name,
                        relative_path = mediaBean.relative_path
                    )
                    albumBean.list.add(mediaBean)
                    albumMap[mediaBean.relative_path] = albumBean
                }
            }
            it.close()
        }
        return albumMap
    }


}