package com.example.app_tienda.routes

import com.example.app_tienda.models.ResponseHttp
import com.example.app_tienda.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRoutes {
    @POST("users/register")
    fun register(@Body user: User):Call<ResponseHttp>
}