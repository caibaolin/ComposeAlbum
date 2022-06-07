package com.cbl.base

import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.room.Room
import com.cbl.base.bean.AlbumBean
import com.cbl.base.bean.AlbumData
import com.cbl.base.bean.GreenManager
import com.cbl.base.bean.MediaBean
import com.cbl.base.dao.AppDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.*
import java.util.concurrent.TimeUnit

private val PICTURE_PATH =
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
private val PICTURE_CONFIG_PATH = PICTURE_PATH + File.separator + ".config"
private val EMPTY_ALBUMS_DATA_FILE_PATH = PICTURE_PATH + File.separator + "emptyAlbum"
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
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.ORIENTATION,
    )

    private fun getMediaBeanFromCursor(cursor: Cursor): MediaBean {
        val path = cursor.getString(0)
        val mimeType = cursor.getString(1)
        var date_modified = cursor.getLong(2)
        var date_added = cursor.getLong(3)
        val duration = cursor.getLong(4)
        val _size = cursor.getLong(5)
        val bucket_display_name = cursor.getString(6)
        val relative_path = cursor.getString(7)
        val id = cursor.getLong(8)
        val orientation = cursor.getInt(9)
        return MediaBean(
            _id = id,
            path = path,
            mimeType = mimeType,
            /*      date_modified=date_modified,
                  date_added=date_added,*/
            duration = duration,
            _size = _size,
/*            bucket_display_name=bucket_display_name,
            relative_path=relative_path,*/
            orientation = orientation
        ).apply {
            this.date_modified = date_modified
            this.date_added = date_added
            bucket_display_name?.let {
                this.bucket_display_name = bucket_display_name
            }
            relative_path?.let {
                this.relative_path = relative_path
            }
        }
    }

    suspend fun getData() = coroutineScope {
        val time = System.currentTimeMillis();
        val deleteAlbumHideFileTask=async { deleteAlbumHideFile() }
        val image_AlbumBeanTask = async { getData2(MediaStore.Images.Media.EXTERNAL_CONTENT_URI) }
        val video_AlbumBeanTask = async { getData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI) }
        val dbTask = async { getDataByDb() }
        val greenListTask = async { GreenManager.getIGreenManager().interceptedFilePathList }
        val emptyAlbumsTask=async { getEmptyAlbums() }
        deleteAlbumHideFileTask.await()
        /*回收站数据*/
        val dbList = dbTask.await()
        /*鉴黄数据*/
        val greenList = greenListTask.await()
        val greenListTemp = mutableListOf<String>()
        greenListTemp.addAll(greenList)
        Timber.i("getData greenList-->${greenList}")
        val dbAlbumBean = AlbumBean(
            displayName = "RECYCLER_IMG_DB",
            list = dbList.toMutableList(),
            relative_path = "RECYCLER_IMG_DB"
        )
        Timber.i("getData dblist.size-->${dbList.size}")
        Timber.i("getData dblist-->${dbList}")
        /*所有图片和视频*/
        val allAlbumBeanMap = mutableMapOf<String, AlbumBean>()
        /*所有图片*/
        val image_AlbumBean = image_AlbumBeanTask.await()
        /*所有视频*/
        val video_AlbumBean = video_AlbumBeanTask.await()
        allAlbumBeanMap.putAll(image_AlbumBean[0])
        video_AlbumBean.forEach {
            if (allAlbumBeanMap.containsKey(it.key)) {
                allAlbumBeanMap[it.key]?.list?.addAll(it.value.list)
            } else {
                allAlbumBeanMap[it.key] = it.value
            }
        }

        /*所有图片和视频的list*/
        val mAlbumBeanList = mutableListOf<AlbumBean>()
        mAlbumBeanList.addAll(allAlbumBeanMap.values)
        mAlbumBeanList.add(dbAlbumBean)

        /*
        * 处理新建空相册逻辑1
        * */
        val emptyAlbums=emptyAlbumsTask.await()
        Timber.i("emptyAlbums frist-->$emptyAlbums")
        var removeCount=0;

        mAlbumBeanList.forEach { albumBean ->
            if(albumBean.list.size>0){
                emptyAlbums.remove(albumBean.relative_path)?.let {
                    removeCount++
                    Timber.i("emptyAlbums remove -->$it")
                }
            }
            /*
            * 处理加密逻辑
            * */
            albumBean.list.forEach {
                if (greenListTemp.contains(it.path)) {
                    greenListTemp.remove(it.path)
                    it.mEncryptFile = true
                }
            }
        }
        /*
        * 处理新建空相册逻辑2
        * */
        if(removeCount>0){
            val list=emptyAlbums.values.toList()
            saveEmptyAlbumsData(list)
        }
        /*
        *
        * 打印耗时
        * */
        launch(Dispatchers.Default){
            Timber.i("getData  mAlbumBeanList-->$mAlbumBeanList")
        }
        Timber.i("emptyAlbums over-->$emptyAlbums")
        /*
        * 发送数据
        * */
        albumData.emit(
            AlbumData(
                alllist = mAlbumBeanList,
                imageAlbums = image_AlbumBean[0],
                imageAlbumsNoGif=image_AlbumBean[1],
                videoAlbums = video_AlbumBean,
                dbAlbumBean = dbAlbumBean,
                greenList = greenList
            )
        )
        Timber.i("getData over-->${System.currentTimeMillis() - time}")
    }

    fun getDataByDb(): List<MediaBean> {
        val db = Room.databaseBuilder(
            BaseApp.CONTEXT,
            AppDatabase::class.java, "internal_recycler.db"
        ).build()
        val time = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30);
        Timber.i("getDataByDb time -->$time")
        return db.mediaBeanDao().getAll(time)
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
//                Timber.i("displayName-->${mediaBean.bucket_display_name} path-->${mediaBean.path}")
                if (albumMap.containsKey(mediaBean.relative_path)) {
                    albumMap[mediaBean.relative_path]?.list?.add(mediaBean)
                } else {
                    val albumBean = AlbumBean(
                        displayName = mediaBean.bucket_display_name,
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
    /**
     * 需要从数据库中获取的信息：
     * BUCKET_DISPLAY_NAME  文件夹名称
     * DATA  文件路径
     */
    private fun getData2(uri: Uri): List<MutableMap<String, AlbumBean>> {
        Timber.i(
            "getData start thread-->${Thread.currentThread().name}  Images uri-->${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}" +
                    "  video uri-->${MediaStore.Video.Media.EXTERNAL_CONTENT_URI} current uri-->${uri}"
        )
        val albumMap = mutableMapOf<String, AlbumBean>()
        val albumMapNogif = mutableMapOf<String, AlbumBean>()
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
//                Timber.i("displayName-->${mediaBean.bucket_display_name} path-->${mediaBean.path}")
                if (albumMap.containsKey(mediaBean.relative_path)) {
                    albumMap[mediaBean.relative_path]?.list?.add(mediaBean)
                } else {
                    val albumBean = AlbumBean(
                        displayName = mediaBean.bucket_display_name,
                        relative_path = mediaBean.relative_path
                    )
                    albumBean.list.add(mediaBean)
                    albumMap[mediaBean.relative_path] = albumBean
                }
                if(mediaBean.mimeType?.contains("gif") == false){
                    if (albumMapNogif.containsKey(mediaBean.relative_path)) {
                        albumMapNogif[mediaBean.relative_path]?.list?.add(mediaBean)
                    } else {
                        val albumBean = AlbumBean(
                            displayName = mediaBean.bucket_display_name,
                            relative_path = mediaBean.relative_path
                        )
                        albumBean.list.add(mediaBean)
                        albumMapNogif[mediaBean.relative_path] = albumBean
                    }
                }
            }
            it.close()
        }
        return listOf(albumMap,albumMapNogif)
    }


    /*
    *
    * 检测并删除nomedia文件
    *
    * */
    private fun deleteAlbumHideFile() {
        deleteAlbumHideFile(Environment.getExternalStorageDirectory().path + File.separator + "DCIM" + File.separator + "Camera")
        deleteAlbumHideFile(Environment.getExternalStorageDirectory().path + File.separator + "DCIM" + File.separator + "Screenshots")
    }
    private fun deleteAlbumHideFile(path: String) {
        try {
            Timber.i(
                "deleteAlbumHideFiles path-->$path"
            )
            if (TextUtils.isEmpty(path)) {
                return
            }
            var tempFile: File? = File(path)
            var hideFile: File? = null
            var scanPath: String? = null
            while (tempFile != null && tempFile.exists()) {
                hideFile = File(tempFile, ".nomedia")
                if (hideFile.exists()) {
                    Timber.i(
                        "deleteAlbumHideFiles hideFile exists-->" + hideFile.path
                    )
                    hideFile.delete()
                    scanPath = tempFile.path
                    Timber.i(
                        "deleteAlbumHideFiles scanPath exists-->$scanPath"
                    )
                }
                tempFile = tempFile.parentFile
            }
            if (scanPath != null) {
                Timber.i(
                    "deleteAlbumHideFiles scanPath end exists-->$scanPath"
                )
                MediaScannerConnection.scanFile(
                    BaseApp.CONTEXT,
                    arrayOf(scanPath),
                    null,
                    null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun saveEmptyAlbumsData(emptyAlbums: List<AlbumBean>) {
        val file = File(PICTURE_PATH)
        if (!file.exists()) {
            file.mkdirs()
        }
        val dataFile =
            File(EMPTY_ALBUMS_DATA_FILE_PATH)
        if (dataFile.exists() && dataFile.isDirectory) {
            dataFile.delete()
        }
        val data = Gson().toJson(emptyAlbums)
        try {
            val fileWriter =
                FileWriter(EMPTY_ALBUMS_DATA_FILE_PATH)
            fileWriter.write(data)
            fileWriter.flush()
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun getEmptyAlbums(): MutableMap<String,AlbumBean> {
        val result= mutableMapOf<String,AlbumBean>()
        val emptyFoldersDataStr = getEmptyAlbumsData()
        Timber.i("emptyFoldersDataStr = $emptyFoldersDataStr")
        if (TextUtils.isEmpty(emptyFoldersDataStr)) {
            return result
        } else {
            try {
                val emptyFolderNames: List<AlbumBean> = Gson().fromJson(
                    emptyFoldersDataStr,
                    object : TypeToken<List<AlbumBean>>() {}.type
                )
                if (emptyFolderNames.isNotEmpty()) {
                    emptyFolderNames.forEach {
                        it.relative_path=Environment.DIRECTORY_PICTURES+File.separator+it.displayName+File.separator
                        result[it.relative_path] = it
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }
    /**
     * @return 手动创建空相册保存起来的json数据
     */
    private fun getEmptyAlbumsData(): String? {
        val file= File(PICTURE_PATH)
        if (!file.exists()) {
            return null
        }
        val dataFile=
            File(EMPTY_ALBUMS_DATA_FILE_PATH)
        if (dataFile.exists() && dataFile.isDirectory) {
            return null
        }
        var fileReader: FileReader? = null
        var buffer: BufferedReader? = null
        val emptyAlbumsDataSB = StringBuilder()
        try {
            fileReader =
                FileReader(EMPTY_ALBUMS_DATA_FILE_PATH)
            buffer = BufferedReader(fileReader)
            var line: String?
            while (buffer.readLine().also { line = it } != null) {
                emptyAlbumsDataSB.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileReader?.close()
                buffer?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return emptyAlbumsDataSB.toString()
    }
}