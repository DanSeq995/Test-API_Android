package it.dsequino.apitest.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import it.dsequino.apitest.models.User

class UserManager {
    private val sharedPreferecesUser: String = "sharedPreferencesUser"
    private val userObject: String = "UserObject"

    companion object {
        private var instance: UserManager? = null

        @Synchronized
        fun getInstance(): UserManager {
            if (instance == null) {
                instance = UserManager()
            }
            return instance!!
        }
    }

    fun saveUser(context: Context, user: User) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferecesUser,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(user)

        editor.putString(userObject, json)
        editor.apply()
    }

    fun loadUser(context: Context): User? {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferecesUser,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val gson = Gson()
        val json = sharedPreferences.getString(userObject, null)
        val user = gson.fromJson(json, User::class.java)

        return user
    }

    fun deleteUser(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferecesUser,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}