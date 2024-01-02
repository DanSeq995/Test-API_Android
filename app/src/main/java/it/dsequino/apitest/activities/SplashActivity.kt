package it.dsequino.apitest.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import it.dsequino.apitest.R
import it.dsequino.apitest.api.ApiException
import it.dsequino.apitest.api.RetrofitTest
import it.dsequino.apitest.databinding.ActivityLoginBinding
import it.dsequino.apitest.databinding.ActivitySplashBinding
import it.dsequino.apitest.models.TextSettingsModel
import it.dsequino.apitest.models.User
import it.dsequino.apitest.utils.Helper
import it.dsequino.apitest.utils.TextSettingsManager
import it.dsequino.apitest.utils.UserManager
import it.sermetra.cloud.laNuovaGuida.models.login.LoginResponse
import org.json.JSONObject

class SplashActivity : AppCompatActivity(), RetrofitTest.Login {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var retrofitTest: RetrofitTest

    private var user: User? = null
    private lateinit var userManager: UserManager

    private var username: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofitTest = RetrofitTest()
        retrofitTest.setLoginListener(this)

        userManager = UserManager.getInstance()
        user = userManager.loadUser(this)

        if (user == null) {
            Helper.startActivity(this, LoginActivity::class.java)
        } else {
            if(isJwtExpired(user?.token!!)) {
                username = user?.username!!
                password = user?.password!!
                retrofitTest.login(username!!, password!!)
            } else {
                Helper.startActivity(this, MainActivity::class.java)
            }
        }
    }

    private fun isJwtExpired(jwtToken: String): Boolean {
        try {
            val splitToken = jwtToken.split(".")
            val base64EncodedBody = splitToken[1]
            val base64DecodedBody = Base64.decode(base64EncodedBody, Base64.URL_SAFE)
            val body = String(base64DecodedBody)
            val jsonObject = JSONObject(body)
            val exp = jsonObject.getLong("exp")
            val currentTimeInSeconds = System.currentTimeMillis() / 1000
            return currentTimeInSeconds > exp
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun login(loginResponse: LoginResponse) {
        userManager.deleteUser(this)
        userManager.saveUser(this, User(loginResponse.id, username, password, loginResponse.role, loginResponse.token))
        Helper.startActivity(this, MainActivity::class.java)
    }

    override fun onError(error: Throwable) {
        if (error is ApiException) {
            Helper.startActivity(this, LoginActivity::class.java)
        }
        error.printStackTrace()
    }
}