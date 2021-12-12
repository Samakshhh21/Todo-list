package com.sampam.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoModel(
    var title:String,
    var description:String,
    var category:String,
    var date:String,
    var time:String,
    var isFinished:Int=0,
    @PrimaryKey(autoGenerate = true)
    var id:Long=0
)