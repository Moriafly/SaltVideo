package com.salt.video.data.entry

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 视频数据量
 */
@Entity
data class Video(

    /** 唯一标识主键 */
    @PrimaryKey
    val url: String,

    /** 标题 */
    val title: String,

    /** 编辑时间 */
    val dateModified: Long

)
