package com.example.app_tienda.models

import com.google.gson.annotations.SerializedName

class User(
    //Mapeamos los datos que contiene la base de datos
    @SerializedName("id") val id:String? = null,
    @SerializedName("name") val name:String,
    @SerializedName("email") val email:String,
   // @SerializedName("image") val image:String? = null,
    @SerializedName("password") val password:String,
    //@SerializedName("repir_password") val repir_password:String
){
    override fun toString(): String {
        return "User(id=$id, name='$name',email='$email', password='$password')"
    }
}