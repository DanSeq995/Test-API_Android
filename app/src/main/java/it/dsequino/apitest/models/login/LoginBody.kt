package it.sermetra.cloud.laNuovaGuida.models.login

import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("username")
    val username: String? = null,

    @SerializedName("password")
    val password: String? = null
)
