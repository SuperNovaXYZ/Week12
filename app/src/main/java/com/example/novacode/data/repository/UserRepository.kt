package com.example.novacode.data.repository

import com.example.novacode.data.dao.UserDao
import com.example.novacode.data.entities.UserEntity
import java.util.UUID
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun login(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    suspend fun register(
        username: String, 
        password: String, 
        isParent: Boolean, 
        email: String? = null,
        parentId: String? = null
    ): Result<UserEntity> {
        return try {
            if (userDao.usernameExists(username)) {
                Result.failure(Exception("Username already exists"))
            } else {
                val user = UserEntity(
                    id = UUID.randomUUID().toString(),
                    username = username,
                    password = password,  // Should be hashed in production
                    isParent = isParent,
                    parentId = parentId,
                    email = email
                )
                userDao.register(user)
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getChildren(parentId: String): Flow<List<UserEntity>> {
        return userDao.getChildren(parentId)
    }

    suspend fun findParentByEmail(email: String): UserEntity? {
        return userDao.findParentByEmail(email)
    }
} 