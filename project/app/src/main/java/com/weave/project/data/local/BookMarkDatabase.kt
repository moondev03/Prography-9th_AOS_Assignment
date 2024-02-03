package com.weave.project.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.weave.project.model.BookMarkEntity

@Database(entities = [BookMarkEntity::class], version = 1)
abstract class BookMarkDatabase : RoomDatabase(){
    abstract fun bookMarkDao(): BookMarkDAO

    companion object {
        private var instance: BookMarkDatabase? = null

        @Synchronized
        fun getInstance(context: Context): BookMarkDatabase? {
            if(instance == null)
                synchronized(BookMarkDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookMarkDatabase::class.java,
                        "BooMark.db"
                    ).build()
                }
            return instance
        }

        fun destroyInstance(){
            instance = null
        }
    }
}