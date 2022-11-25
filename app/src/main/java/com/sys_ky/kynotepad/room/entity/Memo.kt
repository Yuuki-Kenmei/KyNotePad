package com.sys_ky.kynotepad.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MEMO")
data class Memo (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "SORT_ID") val sort_id: Int,
    @ColumnInfo(name = "TEXT") val text: String
)