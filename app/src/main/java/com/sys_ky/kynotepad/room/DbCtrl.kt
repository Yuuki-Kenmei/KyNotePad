package com.sys_ky.kynotepad.room

import android.content.Context
import androidx.room.Room
import com.sys_ky.kynotepad.room.database.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DbCtrl constructor(context: Context) {

    var database: Database

    init {
        database = Room.databaseBuilder(
            context,
            Database::class.java,
            "kynotepad_db"
        ).fallbackToDestructiveMigration().build()
    }

    inner class Memo {
        fun selectAll(): List<com.sys_ky.kynotepad.room.entity.Memo> = runBlocking {
            var rtn: List<com.sys_ky.kynotepad.room.entity.Memo> = listOf()
            val job = CoroutineScope(Dispatchers.IO).launch {
                rtn = database.MemoDao().selectAll()
            }
            job.join()
            return@runBlocking rtn
        }

        fun selectTextById(id: Int): String = runBlocking {
            var rtn: String = ""
            val job = CoroutineScope(Dispatchers.IO).launch {
                rtn = database.MemoDao().selectTextById(id)
            }
            job.join()
            return@runBlocking rtn
        }

        fun selectMaxSortId(): Int = runBlocking {
            var rtn: Int = 0
            val job = CoroutineScope(Dispatchers.IO).launch {
                rtn = database.MemoDao().selectMaxSortId()
            }
            job.join()
            return@runBlocking rtn
        }

        fun selectNextId(): Int = runBlocking {
            var rtn: Int = 0
            val job = CoroutineScope(Dispatchers.IO).launch {
                rtn = database.MemoDao().selectNextId()
            }
            job.join()
            return@runBlocking rtn
        }

        fun selectExistsIdZero(): Boolean = runBlocking {
            var rtn: Boolean = true
            val job = CoroutineScope(Dispatchers.IO).launch {
                if (database.MemoDao().selectExistsIdZero() == 0) {
                    rtn = false
                }
            }
            job.join()
            return@runBlocking rtn
        }

        fun updateTextById(id: Int, text: String) = runBlocking {
            val job = CoroutineScope(Dispatchers.IO).launch {
                database.MemoDao().updateTextById(id, text)
            }
            job.join()
            return@runBlocking
        }

        fun deleteById(id: Int) = runBlocking {
            val job = CoroutineScope(Dispatchers.IO).launch {
                database.MemoDao().deleteById(id)
            }
            job.join()
            return@runBlocking
        }

        fun updateSortId(id: Int, oldSortId: Int, newSortId: Int) = runBlocking {
            val job = CoroutineScope(Dispatchers.IO).launch {
                database.MemoDao().updateSortId(id, oldSortId, newSortId)
            }
            job.join()
            return@runBlocking
        }

        fun insertAll(memo: com.sys_ky.kynotepad.room.entity.Memo) = runBlocking {
            val job = CoroutineScope(Dispatchers.IO).launch {
                database.MemoDao().insertAll(memo)
            }
            job.join()
            return@runBlocking
        }
    }

    companion object {
        private var instance: DbCtrl? = null

        fun getInstance(context: Context?): DbCtrl {
            if (instance == null && context != null) {
                instance = DbCtrl(context)
            }
            return instance!!
        }
    }
}