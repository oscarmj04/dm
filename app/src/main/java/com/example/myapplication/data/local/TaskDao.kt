package com.example.myapplication.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY category, dueDate")
    fun getAll(): LiveData<List<TaskEntity>>

    @Insert
    suspend fun insert(entity: TaskEntity): Long

    @Update
    suspend fun update(entity: TaskEntity)

    @Delete
    suspend fun delete(entity: TaskEntity)

    @Query("UPDATE tasks SET remoteId = :remoteId WHERE localId = :localId")
    suspend fun updateRemoteId(localId: Long, remoteId: String)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(list: List<TaskEntity>)

    @Transaction
    suspend fun replaceAll(list: List<TaskEntity>) {
        deleteAll()
        insertAll(list)
    }
}
