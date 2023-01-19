package com.salt.video.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.salt.video.data.dao.VideoDao
import com.salt.video.data.entry.MediaSource
import com.salt.video.data.entry.Video

/**
 * The Room database for this app
 */
@Database(
    entities = [
        Video::class,
        MediaSource::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE Song ADD COLUMN playedTimes INTEGER NOT NULL DEFAULT 0")
//
//                database.execSQL("CREATE TABLE Listening (id TEXT NOT NULL PRIMARY KEY, year INTEGER NOT NULL DEFAULT 0, month INTEGER NOT NULL DEFAULT 0, day INTEGER NOT NULL DEFAULT 0, hour INTEGER NOT NULL DEFAULT 0, minute INTEGER NOT NULL DEFAULT 0, songId TEXT NOT NULL DEFAULT \"\", duration INTEGER NOT NULL DEFAULT 0)")
//            }
//        }

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DB_NAME
            )
                // .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
                .fallbackToDestructiveMigration() // 上线移除
                .build().also {
                    instance = it
                }
        }

        const val DB_NAME = "app_database"
    }
}