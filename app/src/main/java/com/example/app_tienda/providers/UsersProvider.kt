package com.example.app_tienda.providers

import retrofit2.Call
import com.example.app_tienda.api.ApiRouts
import com.example.app_tienda.models.ResponseHttp
import com.example.app_tienda.models.User
import com.example.app_tienda.routes.UserRoutes

class UsersProvider {
    private var userRoutes: UserRoutes? = null

    // Constructor en Kotlin
    init {
        val api = ApiRouts()
        userRoutes = api.getUsersRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {
        return userRoutes?.register(user)
    }

    fun login(email:String, password:String): Call<ResponseHttp>? {
        return userRoutes?.login(email,password)
    }
}




























