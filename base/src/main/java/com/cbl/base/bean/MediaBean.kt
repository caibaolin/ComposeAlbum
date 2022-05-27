package com.cbl.base.bean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/5/25 17:28
 *     desc   :
 * </pre>
 */
data class MediaBean(
    val path: String,
    val mimeType: String,
    val date_modified: Long,
    val date_added: Long,
    val duration: Long,
    val _size:Long,
    val bucket_display_name: String,
    val relative_path:String,
    )
