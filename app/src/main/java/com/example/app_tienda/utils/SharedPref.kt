package com.example.app_tienda.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPref(activity: Activity){

    private var prefs: SharedPreferences? = null

    init {
        prefs = activity.getSharedPreferences("com.example.app_tienda", Context.MODE_PRIVATE)
    }

    // Guardar información en la sesión
    fun save(key: String, objeto: Any) {
        try {
            val gson = Gson()
            val json = gson.toJson(objeto)
            prefs?.edit()?.putString(key, json)?.apply()
        } catch (e: Exception) {
            Log.d("SharedPref", "Error: ${e.message}")
        }
    }

    // Obtener información en forma de String
    fun getData(key: String): String? {
        return prefs?.getString(key, null)
    }

    // Obtener información deserializada
    inline fun <reified T> getObject(key: String): T? {
        val json = getData(key)
        return if (json.isNullOrEmpty()) {
            null
        } else {
            try {
                Gson().fromJson(json, object : TypeToken<T>() {}.type)
            } catch (e: Exception) {
                Log.d("SharedPref", "Error deserializing object: ${e.message}")
                null
            }
        }
    }

    // Eliminar información de la sesión
    fun remove(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }
}
