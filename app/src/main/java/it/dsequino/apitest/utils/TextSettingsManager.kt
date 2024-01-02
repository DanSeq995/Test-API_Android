package it.dsequino.apitest.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import it.dsequino.apitest.models.TextSettingsModel
import it.dsequino.apitest.utils.TextSettings as TextSettings

class TextSettingsManager {
    private val sharedPreferecesTextSettings: String = "sharedPreferencesTextSettings"
    private val textSettingsObject: String = "TextSettingsObject"

    companion object {
        private var instance: TextSettingsManager? = null

        @Synchronized
        fun getInstance(): TextSettingsManager {
            if (instance == null) {
                instance = TextSettingsManager()
            }
            return instance!!
        }
    }

    fun saveTextSettings(context: Context, textSettings: TextSettingsModel) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferecesTextSettings,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(textSettings)

        editor.putString(textSettingsObject, json)
        editor.apply()
    }

    fun loadTextSettings(context: Context): TextSettingsModel? {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferecesTextSettings,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val gson = Gson()
        val json = sharedPreferences.getString(textSettingsObject, null)

        return gson.fromJson(json, TextSettingsModel::class.java)
    }

    fun deleteTextSettings(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferecesTextSettings,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}