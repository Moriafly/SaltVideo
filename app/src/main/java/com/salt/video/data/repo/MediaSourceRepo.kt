package com.salt.video.data.repo

import com.salt.video.App
import com.salt.video.data.entry.MediaSource
import kotlinx.coroutines.flow.Flow

object MediaSourceRepo {

    suspend fun insert(mediaSource: MediaSource) = dao.insert(mediaSource)

    fun getAll(): Flow<List<MediaSource>> = dao.getAll()

    private val dao = App.appDatabase.mediaSourceDao()

}