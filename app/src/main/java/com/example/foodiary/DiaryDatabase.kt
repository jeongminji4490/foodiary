package com.example.foodiary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**다이어리 DB**/
@Database(
    entities = [morningDiary::class, lunchDiary::class, dinnerDiary::class, date::class],
    version = 1,
    exportSchema = false
)
abstract class DiaryDatabase: RoomDatabase() {
    abstract fun DiaryDao(): DiaryDao

    companion object{
        private var instance: DiaryDatabase?=null

        //Synchronized : 다수의 쓰레드가 한 개의 자원을 사용하고자 할 때, 한 개의 쓰레드만 제외하고 나머지는 접근을 못하도록 막는 것
        //보통 메소드의 선언부에 쓰고 이 키워드가 붙은 메서드는 한 번에 하나의 스레드만 접근이 가능하며 메서드가 사용 중일 때 다른 스레드가 메서드를 호출하면 앞의 스레드가 종료될 때까지 기다려야함
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