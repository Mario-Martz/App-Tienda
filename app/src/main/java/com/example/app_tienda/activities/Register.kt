package com.example.app_tienda.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_tienda.R
import com.example.app_tienda.models.ResponseHttp
import com.example.app_tienda.models.User
import com.example.app_tienda.providers.UsersProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    private var layoutName: TextInputLayout? = null
    private var layoutEmail: TextInputLayout? = null
    private var layoutPass: TextInputLayout? = null
    private var layoutRepitPass: TextInputLayout? = null

    private var editName: TextInputEditText? = null
    private var editEmail: TextInputEditText? = null
    private var editPass: TextInputEditText? = null
    private var editRepiPass: TextInputEditText? = null

    private lateinit var usersProvider: UsersProvider

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicialización de vistas
        layoutName = findViewById(R.id.txtInpLayoutName)
        layoutEmail = findViewById(R.id.txtInpLayoutEmail)
        layoutPass = findViewById(R.id.txtInpLayoutPassword)
        layoutRepitPass = findViewById(R.id.txtInpLayoutRepitPassword)

        editName = findViewById(R.id.txtInpEdtName)
        editEmail = findViewById(R.id.txtInpEdtEmail)
        editPass = findViewById(R.id.txtInpEdtPassword)
        editRepiPass = findViewById(R.id.txtInpEdtRepitPassword)

        val btnCrear: Button = findViewById(R.id.btnContinura)
        val viewRegister: TextView = findViewById(R.id.Inicra_sesion)
        btn_Regresar(viewRegister)

        usersProvider = UsersProvider()  // Inicialización de UsersProvider

        editPass?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                layoutPass?.helperText = "Usa letras mayúsculas, minúsculas, números y caracteres especiales"
            } else {
                layoutPass?.helperText = null
            }
        }

        editRepiPass?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                layoutRepitPass?.helperText = "Recuerda que las contraseñas tienen que coincidir"
            } else {
                layoutRepitPass?.helperText = null
            }
        }

        btnCrear.setOnClickListener {
            val name = editName?.text.toString()
            val email = editEmail?.text.toString()
            val pass = editPass?.text.toString()
            val repPass = editRepiPass?.text.toString()
            if (isValido(name, email, pass, repPass)) {
                val user = User(null, name, email, pass)
                registerUser(user)
            } else {
                Toast.makeText(this, "Por favor, corrija los errores", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun btn_Regresar(viewRegister: TextView) {
        viewRegister.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun String.isEmailValid(): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|outlook\\.com|microsoft\\.com|zoho\\.com|fastmail\\.com)")
        return emailRegex.matches(this)
    }

    private fun String.isPassValid(): Boolean {
        val passRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*()\\-_=+,.?\":;<>|\\[\\]{}~]).{8,}$")
        return passRegex.matches(this)
    }

    private fun isValido(name: String, email: String, pass: String, repPass: String): Boolean {
        var isValid = true

        if (name.isBlank()) {
            layoutName?.error = "Campo obligatorio"
            isValid = false
        } else {
            layoutName?.isErrorEnabled = false
        }

        if (email.isBlank()) {
            layoutEmail?.error = "Campo obligatorio"
            isValid = false
        } else if (!email.isEmailValid()) {
            layoutEmail?.error = "Email no válido"
            isValid = false
        } else {
            layoutEmail?.isErrorEnabled = false
        }

        if (pass.isBlank()) {
            layoutPass?.error = "Campo obligatorio"
            isValid = false
        } else if (!pass.isPassValid()) {
            layoutPass?.error = "Contraseña no válida"
            isValid = false
        } else {
            layoutPass?.isErrorEnabled = false
        }

        if (repPass.isBlank()) {
            layoutRepitPass?.error = "Campo obligatorio"
            isValid = false
        } else if (pass != repPass) {
            layoutRepitPass?.error = "Las contraseñas no coinciden"
            isValid = false
        } else {
            layoutRepitPass?.isErrorEnabled = false
        }

        return isValid
    }

    private fun registerUser(user: User) {
        usersProvider.register(user)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Register, response.body()?.message, Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Register, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@Register, "Error en el registro", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d(TAG,"Se prodijo un error ${t.message}")
                Toast.makeText(this@Register, "Error en la conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
