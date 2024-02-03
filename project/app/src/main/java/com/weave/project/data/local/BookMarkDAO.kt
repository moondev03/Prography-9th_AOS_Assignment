package com.weave.project.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weave.project.model.BookMarkEntity

interface BookMarkDAO {
    @Query("SELECT * FROM table_bookmark")
    fun getAll(): List<BookMarkEntity>

    @Query("SELECT * FROM table_bookmark WHERE id = :id")
    fun getBookMark(id: String): BookMarkEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBookMark(bookMarkEntity: BookMarkEntity)

    @Query("DELETE FROM table_bookmark WHERE id = :id")
    fun deleteBookMark(id: String)
}