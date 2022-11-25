package com.sys_ky.kynotepad.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sys_ky.kynotepad.room.dao.*
import com.sys_ky.kynotepad.room.entity.*

@Database(entities = [
    Memo::class],
    exportSchema = false,
    version = 1)
abstract class Database: RoomDatabase() {
    abstract fun MemoDao(): MemoDao
}