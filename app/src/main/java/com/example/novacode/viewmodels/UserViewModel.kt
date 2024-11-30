package com.example.novacode.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.novacode.data.AppDatabase
import com.example.novacode.data.entities.UserEntity
import com.example.novacode.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UserRepository(database.userDao())
    }

    fun login(username: String, password: String, isParent: Boolean) {
        viewModelScope.launch {
            try {
                val user = repository.login(username, password)
                if (user != null && user.isParent == isParent) {
                    _loginState.value = LoginState.Success(user)
                } else {
                    _loginState.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(username: String, password: String, isParent: Boolean, email: String?, parentId: String? = null) {
        viewModelScope.launch {
            try {
                val result = repository.register(
                    username = username,
                    password = password,
                    isParent = isParent,
                    email = email,
                    parentId = parentId
                )
                result.fold(
                    onSuccess = { _loginState.value = LoginState.Success(it) },
                    onFailure = { _loginState.value = LoginState.Error(it.message ?: "Registration failed") }
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Registration failed")
            }
        }
    }

    sealed class LoginState {
        object Initial : LoginState()
        data class Success(val user: UserEntity) : LoginState()
        data class Error(val message: String) : LoginState()
    }
} 