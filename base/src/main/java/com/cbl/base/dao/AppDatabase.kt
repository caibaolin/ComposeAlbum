package com.cbl.base.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cbl.base.bean.MediaBean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/31 16:32
 *     desc   :
 * </pre>
 */
@Database(entities = [MediaBean::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaBeanDao(): MediaBeanDao
}