package com.example.app_tienda.api

import com.example.app_tienda.routes.UserRoutes
import retrofit2.create

class ApiRouts {
    //val Api_URL = "http://192.168.0.58:3500/api/"
    val Api_URL = "http://192.168.1.81:3500/api/"
    //Creamos contantes
    var retrofit = RetrofitCliente()

    fun getUsersRoutes():UserRoutes{
        return retrofit.getCliente(Api_URL).create(UserRoutes::class.java)
    }
}