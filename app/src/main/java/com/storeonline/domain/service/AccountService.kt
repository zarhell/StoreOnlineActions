package com.storeonline.domain.service

import android.content.Context
import com.storeonline.domain.model.User
import com.storeonline.infrastructure.repository.AccountRepository

class AccountService(context: Context) {
    private val accountRepository = AccountRepository(context)


    fun registerUser(user: User): Boolean {
        return accountRepository.registerUser(user)
    }

    fun authenticateUser(username: String, password: String): Boolean {
        return accountRepository.authenticateUser(username, password)
    }

    fun isSessionActive(): Boolean {
        return accountRepository.isSessionActive()
    }

    fun doesUserExist(username: String): Boolean {
        return accountRepository.doesUserExist(username)
    }

    fun deleteUser(username: String): Boolean {
        return accountRepository.deleteUser(username)
    }

    fun logoutUser() {
        accountRepository.logoutUser()
    }
}
