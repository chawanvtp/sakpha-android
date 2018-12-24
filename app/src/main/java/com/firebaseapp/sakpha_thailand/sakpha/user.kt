package com.firebaseapp.sakpha_thailand.sakpha

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: Int,
    val description: String
)