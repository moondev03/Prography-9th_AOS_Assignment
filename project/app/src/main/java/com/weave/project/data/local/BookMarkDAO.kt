package com.weave.project.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weave.project.model.BookMarkEntity

@Dao
interface BookMarkDAO {
    // 모든 북마크
    @Query("SELECT * FROM table_bookmark")
    fun getAll(): List<BookMarkEntity>

    // id 값을 사용해 해당 북마크 유무 체크 ( 0 -> 북마크 아님 )
    @Query("SELECT COUNT(*) FROM table_bookmark WHERE id = :id")
    fun getBookMark(id: String): Int

    // 북마크 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBookMark(bookMarkEntity: BookMarkEntity)

    // 해당 id에 해당하는 북마크 제거
    @Query("DELETE FROM table_bookmark WHERE id = :id")
    fun deleteBookMark(id: String)
}