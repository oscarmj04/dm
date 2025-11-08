package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    // Reactivo (para lista)
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAll(): LiveData<List<Task>>

    // Reactivo (para detalle/edición por id)
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeById(id: Int): LiveData<Task>

    // No reactivo (uso interno puntual)
    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Task?

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Int)
}
