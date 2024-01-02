package it.dsequino.apitest.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("token")
    val token: String? = null
)