package me.tbsten.tripleTriad.data.database.example.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val uid: Int,

    @ColumnInfo(name = "name")
    val name: String?,
)
