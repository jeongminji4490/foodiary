package com.example.foodiary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [morningDiary::class, lunchDiary::class, dinnerDiary::class],
    version = 1,
    exportSchema = false
)
abstract class DiaryDatabase: RoomDatabase() {
    abstract fun DiaryDao(): DiaryDao

    companion object{
        private var instance: DiaryDatabase?=null

        @Synchronized
        fun getInstance(context: Context): DiaryDatabase? {
            if (instance == null){
                synchronized(DiaryDatabase::class){
                    instance=Room.databaseBuilder(
                        context.applicationContext,
                        DiaryDatabase::class.java,
                        "diary_database"
                    ).build()
                }
            }
            return instance
        }
    }

}