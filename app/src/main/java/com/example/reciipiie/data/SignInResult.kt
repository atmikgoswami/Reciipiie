package com.example.reciipiie.data

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val email: String,
    val profilePictureUrl: String?
)