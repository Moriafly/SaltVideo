package com.salt.video.data.entry

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 媒体来源
 */
@Entity
data class MediaSource (

    @PrimaryKey
    val url: String

)