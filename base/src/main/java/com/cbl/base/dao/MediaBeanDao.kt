package com.cbl.base.dao

import androidx.room.Dao
import androidx.room.Query
import com.cbl.base.bean.MediaBean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/31 16:31
 *     desc   :
 * </pre>
 */
@Dao
interface MediaBeanDao {
    @Query("SELECT * FROM RECYCLER_IMG_DB WHERE RECYCLE_TIME> :mRecycleTime ORDER BY RECYCLE_TIME ASC")
    fun getAll(mRecycleTime:Long): List<MediaBean>
}