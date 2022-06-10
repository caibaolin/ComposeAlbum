package com.cbl.base.bean

import androidx.annotation.Nullable
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.contracts.InvocationKind

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/25 17:28
 *     desc   :
 * </pre>
 *
 *   private Long id;
private String oldPath;
private String path;
private String oldName;
private String name;
private long fileSize;
private long oldAddTime;
private long recycleTime;
private String mimeType;
private int orientation;
private int fileType;
private long duration;
private long takenTime;
private long oldId;
private int isEncryptFile;

public static final String INDEX_ID = "_id";
public static final String INDEX_OLD_PATH = "OLD_PATH";
public static final String INDEX_PATH = "PATH";
public static final String INDEX_OLD_NAME = "OLD_NAME";
public static final String INDEX_NAME = "NAME";
public static final String INDEX_FILESIZE = "FILE_SIZE";
public static final String INDEX_OLD_ADDTIME = "OLD_ADD_TIME";
public static final String INDEX_RECYCLETIME = "RECYCLE_TIME";
public static final String INDEX_MIMETYPE = "MIME_TYPE";
public static final String INDEX_ORIENTATION = "ORIENTATION";
public static final String INDEX_FILETYPE = "FILE_TYPE";
public static final String INDEX_DURATION = "DURATION";
public static final String INDEX_TAKENTIME = "TAKEN_TIME";
public static final String INDEX_OLD_ID = "OLD_ID";
 */
@Stable
@Entity(tableName = "RECYCLER_IMG_DB")
data class MediaBean(
    @PrimaryKey val _id: Long?,
    @ColumnInfo(name = "PATH")val path: String?,
    @ColumnInfo(name = "OLD_PATH")val old_Path: String?="",
    @ColumnInfo(name = "NAME")val name: String?="",
    @ColumnInfo(name = "OLD_NAME")val old_name: String?="",
    @ColumnInfo(name = "MIME_TYPE")val mimeType: String?,

    @ColumnInfo(name = "DURATION")val duration: Long,
    @ColumnInfo(name = "FILE_SIZE")val _size:Long,
    @ColumnInfo(name = "OLD_ADD_TIME")val old_add_time:Long=-1,
    @ColumnInfo(name = "RECYCLE_TIME")val RECYCLE_TIME:Long=-1,
    @ColumnInfo(name = "TAKEN_TIME")val TAKEN_TIME:Long=-1,
    @ColumnInfo(name = "OLD_ID")val OLD_ID:Long=-1,
    @ColumnInfo(name = "IS_ENCRYPT_FILE")val IS_ENCRYPT_FILE:Long=-1,

    @ColumnInfo(name = "ORIENTATION")val orientation:Int,
    @ColumnInfo(name = "FILE_TYPE")val FILE_TYPE:Int=-1,

    ){
    @Ignore
    var date_modified: Long=-1
    @Ignore
    var date_added: Long=1
    @Ignore
    var bucket_display_name: String=""
    @Ignore
    var relative_path:String=""
    @Ignore
    var mEncryptFile:Boolean=false
    override fun toString(): String {
        return "MediaBean(_id=$_id, path=$path, old_Path=$old_Path, name=$name, old_name=$old_name, mimeType=$mimeType, duration=$duration, _size=$_size, old_add_time=$old_add_time, RECYCLE_TIME=$RECYCLE_TIME, TAKEN_TIME=$TAKEN_TIME, OLD_ID=$OLD_ID, IS_ENCRYPT_FILE=$IS_ENCRYPT_FILE, orientation=$orientation, FILE_TYPE=$FILE_TYPE, date_modified=$date_modified, date_added=$date_added, bucket_display_name='$bucket_display_name', relative_path='$relative_path', mEncryptFile=$mEncryptFile)"
    }

}
