package com.weave.project.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weave.project.model.BookMarkEntity

@Dao
interface BookMarkDAO {
    @Query("SELECT * FROM table_bookmark")
    fun getAll(): List<BookMarkEntity>

    @Query("SELECT COUNT(*) FROM table_bookmark WHERE id = :id")
    fun getBookMark(id: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBookMark(bookMarkEntity: BookMarkEntity)

    @Query("DELETE FROM table_bookmark WHERE id = :id")
    fun deleteBookMark(id: String)
}