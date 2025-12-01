package com.example.task_manager_app.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY priority DESC, createdAt DESC")
    fun getAllTodos(): LiveData<List<TodoEntity>>

    @Query("SELECT * FROM todo WHERE id = :id")
    fun getTodoById(id: Long): LiveData<TodoEntity?>

    @Query("SELECT * FROM todo WHERE isCompleted = 0 ORDER BY priority DESC, createdAt DESC")
    fun getIncompleteTodos(): LiveData<List<TodoEntity>>

    @Query("SELECT * FROM todo WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTodos(): LiveData<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity): Long

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("DELETE FROM todo")
    suspend fun deleteAllTodos()

    @Query("UPDATE todo SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletionStatus(id: Long, isCompleted: Boolean)

}