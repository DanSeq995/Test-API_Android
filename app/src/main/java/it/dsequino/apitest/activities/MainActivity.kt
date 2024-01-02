package it.dsequino.apitest.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.dsequino.apitest.R
import it.dsequino.apitest.databinding.ActivityMainBinding
import it.dsequino.apitest.databinding.ActivitySplashBinding
import it.dsequino.apitest.models.User
import it.dsequino.apitest.utils.UserManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var user: User? = null
    private lateinit var userManager: UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager()
        user = userManager.loadUser(this)

        if (user?.role == "admin") {

        }
    }
}