package com.example.novacode.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val password: String,
    val isParent: Boolean,
    val parentId: String? = null,
    val email: String? = null
) 