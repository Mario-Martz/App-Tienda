package com.example.app_tienda.routes

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.app_tienda.models.ResponseHttp
import com.example.app_tienda.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Field

interface UserRoutes {
    @POST("users/register")
    fun register(@Body user: User):Call<ResponseHttp>

    //
    @POST("users/login")
    fun login(@Field("email")email: String,@Field("password")password:String):Call<ResponseHttp>
}