package com.sys_ky.kynotepad.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sys_ky.kynotepad.room.entity.Memo

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo ORDER BY sort_id")
    fun selectAll(): List<Memo>

    @Query("SELECT text FROM memo WHERE id = :id")
    fun selectTextById(id: Int): String

    @Query("SELECT IFNULL(MAX(sort_id), -1) FROM memo")
    fun selectMaxSortId(): Int

    @Query("SELECT IFNULL(MIN(id), -1) + 1 FROM memo WHERE NOT EXISTS(SELECT * FROM memo as tmp WHERE tmp.id = memo.id + 1)")
    fun selectNextId(): Int

    @Query("SELECT COUNT(*) FROM memo WHERE id = 0")
    fun selectExistsIdZero(): Int

    @Query("UPDATE memo SET text = :text WHERE id = :id")
    fun updateTextById(id: Int, text: String)

    @Query("DELETE FROM memo WHERE id = :id")
    fun deleteById(id: Int)

    @Query("UPDATE memo SET sort_id = CASE WHEN id = :id THEN :newSortId WHEN :oldSortId < :newSortId AND sort_id > :oldSortId AND sort_id <= :newSortId THEN sort_id - 1 WHEN :oldSortId > :newSortId AND sort_id < :oldSortId AND sort_id >= :newSortId THEN sort_id + 1 ELSE sort_id END")
    fun updateSortId(id: Int, oldSortId: Int, newSortId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg baseSetting: Memo)
}