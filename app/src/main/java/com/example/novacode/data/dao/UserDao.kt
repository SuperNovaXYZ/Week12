package com.example.novacode.data.dao

import androidx.room.*
import com.example.novacode.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: UserEntity)

    @Query("SELECT * FROM users WHERE parentId = :parentId")
    fun getChildren(parentId: String): Flow<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    suspend fun usernameExists(username: String): Boolean

    @Query("SELECT * FROM users WHERE email = :email AND isParent = 1 LIMIT 1")
    suspend fun findParentByEmail(email: String): UserEntity?
} 