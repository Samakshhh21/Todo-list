package com.sampam.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoModel::class], version = 1)
abstract class tododb:RoomDatabase(){
    abstract fun tododao():todoDao

    companion object{
        @Volatile
        private var INSTANCE:tododb?=null
        fun getdatabase(context: Context):tododb{
            val tempInstance= INSTANCE
            if(tempInstance !=null){
                return tempInstance
            }
            synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                tododb::class.java,
                todotask).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}