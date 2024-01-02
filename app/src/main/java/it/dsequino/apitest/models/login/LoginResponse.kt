package it.sermetra.cloud.laNuovaGuida.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("token")
    val token: String? = null
)
