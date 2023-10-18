package com.example.midtermapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 *
 * Defines the primary key and additional columns of each Task, stored in a database
 */
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,
    @ColumnInfo(name = "task_name")
    var taskName: String = "",
    @ColumnInfo(name = "task_description")
    var taskDescription: String = ""
)
