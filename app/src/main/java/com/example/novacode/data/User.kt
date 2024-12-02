package com.example.novacode.data

data class User(
    val id: String,
    val username: String,
    val isParent: Boolean,
    val parentId: String? = null // For child accounts, links to parent
)

data class GameProgress(
    val userId: String,
    val levelId: Int,
    val gameId: Int,
    val completed: Boolean,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis(),
    )