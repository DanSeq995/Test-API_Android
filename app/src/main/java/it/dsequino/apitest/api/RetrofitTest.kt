package it.dsequino.apitest.api

import com.google.gson.GsonBuilder
import it.dsequino.apitest.models.Root
import it.dsequino.apitest.models.User
import it.sermetra.cloud.laNuovaGuida.models.login.LoginBody
import it.sermetra.cloud.laNuovaGuida.models.login.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class RetrofitTest {
    //region VARIABLES
    private lateinit var login: Login
    private var retrofit: Retrofit? = null
    private val baseUrl = "http://192.168.1.57:3000/"
    //endregion

    //region GET CLIENT
    private fun getClient(): Retrofit {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()


            val gson = GsonBuilder().create()

            // Custom Converter Factory to handle null or empty responses
            val nullOnEmptyConverterFactory = object : Converter.Factory() {
                override fun responseBodyConverter(
                    type: Type,
                    annotations: Array<Annotation>,
                    retrofit: Retrofit
                ): Converter<ResponseBody, *>? {
                    val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
                    return Converter { body ->
                        if (body.contentLength() == 0L) null
                        else delegate.convert(body)
                    }
                }
            }

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }
    //endregion

    //region RESPONSE HANDLER
    private abstract class ResponseHandler<T> : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) {
            if (response.isSuccessful) {
                val body: T? = response.body()
                onResponse(body)
            } else {
                try {
                    val errorBodyStr = response.errorBody()?.string()
                    if (!errorBodyStr.isNullOrEmpty()) {
                        val errorObject = JSONObject(errorBodyStr)
                        onError(
                            ApiException(
                                errorObject.getInt("error_code"),
                                errorObject.getString("error_message")
                            )
                        )
                    } else {
                        // Handle the case where the error body is null or empty
                        onResponse(null)
                    }
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onError(t)
        }

        abstract fun onResponse(response: T?)
        abstract fun onError(error: Throwable)
    }

    interface OnErrorListener {
        fun onError(error: Throwable)
    }
    //endregion

    //region LOGIN
    fun login(username: String, password: String) {
        val lngApi = getClient().create(TestApi::class.java)
        val body = LoginBody(username, password)
        val call = lngApi.login(body)

        call.enqueue(object : ResponseHandler<LoginResponse>() {
            override fun onResponse(response: LoginResponse?) {
                login.login(response!!)
            }

            override fun onError(error: Throwable) {
                login.onError(error)
            }

        })
    }
    interface Login : OnErrorListener {
        fun login(loginResponse: LoginResponse)
    }
    fun setLoginListener(login: Login) {
        this.login = login
    }
    //endregion
}