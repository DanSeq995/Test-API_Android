package it.dsequino.apitest.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import it.dsequino.apitest.R
import it.dsequino.apitest.api.ApiException
import it.dsequino.apitest.api.RetrofitTest
import it.dsequino.apitest.databinding.ActivityLoginBinding
import it.dsequino.apitest.models.User
import it.dsequino.apitest.utils.Helper
import it.dsequino.apitest.utils.TextSettings
import it.dsequino.apitest.utils.UserManager
import it.sermetra.cloud.laNuovaGuida.models.login.LoginResponse

class LoginActivity : AppCompatActivity(), RetrofitTest.Login {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var textSettings: TextSettings

    private lateinit var retrofitTest: RetrofitTest

    private lateinit var userManager: UserManager

    private var username: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofitTest = RetrofitTest()
        retrofitTest.setLoginListener(this)

        userManager = UserManager.getInstance()

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
                clearFocus()
            }
            false
        }

        setText()
        setupEditTextFocusListeners()
        setupShowHidePasswordListener()

        binding.btnLogin.setOnClickListener {
            username = binding.editTextName.text.toString()
            password = binding.editTextPassword.text.toString()
            retrofitTest.login(username!!, password!!)
        }
    }

    private fun setText() {
        textSettings = TextSettings(this)

        textSettings.setFont(binding.loginText, "bold")
        textSettings.setTextSize(binding.loginText)

        textSettings.setFont(binding.editTextName, "regular")
        textSettings.setTextSize(binding.editTextName)

        textSettings.setFont(binding.editTextPassword, "regular")
        textSettings.setTextSize(binding.editTextPassword)

        textSettings.setFont(binding.btnText, "regular")
        textSettings.setTextSize(binding.btnText)
    }

    private fun setupEditTextFocusListeners() {
        val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (view is EditText) {
                if (hasFocus) {
                    view.hint = "" // Nasconde l'hint quando l'EditText guadagna il focus
                } else if (view.text.isEmpty()) {
                    // Mostra di nuovo l'hint se l'EditText perde il focus ed è vuoto
                    view.hint = when (view.id) {
                        R.id.editTextName -> getString(R.string.name)
                        R.id.editTextPassword -> getString(R.string.password)
                        else -> ""
                    }
                }
            }
        }

        binding.editTextName.onFocusChangeListener = focusChangeListener
        binding.editTextPassword.onFocusChangeListener = focusChangeListener
    }

    // Nasconde la tastiera e rimuove il focus da tutti gli EditText
    private fun hideKeyboard() {
        val view = this.currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    // Rimuove il focus da tutti gli EditText
    private fun clearFocus() {
        binding.editTextName.clearFocus()
        binding.editTextPassword.clearFocus()
    }

    private fun setupShowHidePasswordListener() {
        binding.imageViewEye.setOnClickListener {
            if (binding.editTextPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // Password attualmente visibile, nascondila e cambia icona
                binding.editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.imageViewEye.setImageResource(R.drawable.icon_eye)
            } else {
                // Password attualmente nascosta, mostrala e cambia icona
                binding.editTextPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.imageViewEye.setImageResource(R.drawable.icon_eye_slash)
            }

            // Sposta il cursore alla fine del testo
            binding.editTextPassword.setSelection(binding.editTextPassword.text.length)
        }
    }

    override fun login(loginResponse: LoginResponse) {
        userManager.deleteUser(this)
        userManager.saveUser(this, User(loginResponse.id, username, password, loginResponse.role, loginResponse.token))
        Helper.startActivity(this, MainActivity::class.java)
    }

    override fun onError(error: Throwable) {
        if (error is ApiException) {
            println(error)
        }
        error.printStackTrace()
    }
}