package it.dsequino.apitest.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
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

        val navHostFragment = NavHostFragment.create(
            when (user?.role) {
            "admin" -> R.navigation.admin_navigation_map
            "foreman" -> R.navigation.foreman_navigation_map
             else -> return
            }
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_container, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commit()
    }
}
