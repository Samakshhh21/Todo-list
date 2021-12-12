package com.sampam.myapplication

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface todoDao{
    @Insert()
    suspend fun insertTask(todoModel: TodoModel):Long

    @Query("select * from TodoModel where isFinished ==0 ")
     fun get():LiveData<List<TodoModel>>

    @Query("update TodoModel set isFinished=1 where id=:uid")
    fun finishtask(uid:Long)

    @Query("delete from TodoModel where id=:uid")
    fun deletetask(uid:Long)
}