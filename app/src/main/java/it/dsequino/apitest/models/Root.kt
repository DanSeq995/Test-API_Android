package it.dsequino.apitest.models

import com.google.gson.annotations.SerializedName

data class Root<T>(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("data")
    val data: T? = null
)