package com.example.app_tienda.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.app_tienda.R
import com.example.app_tienda.models.ResponseHttp
import com.example.app_tienda.models.User
import com.example.app_tienda.providers.UsersProvider
import com.example.app_tienda.utils.SharedPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    var editextEmail: TextInputEditText? = null
    var editextPass: TextInputEditText? = null

    var inputLayoutEmail: TextInputLayout? = null
    var inputLayoutPass: TextInputLayout? = null

    var usersProvider = UsersProvider()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val btnGoogle: Button = findViewById(R.id.btnGoogle)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val viewRegister: TextView = findViewById(R.id.Regristra)

        editextEmail = findViewById(R.id.EditTextEmail)
        editextPass = findViewById(R.id.EditTextPass)

        inputLayoutEmail = findViewById(R.id.textInputLayoutEmail)
        inputLayoutPass = findViewById(R.id.textInputLayoutPassword)

        btnRegister(viewRegister)

        btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        btnLogin.setOnClickListener {
            val email = editextEmail?.text.toString()
            val pass = editextPass?.text.toString()
            if (isValid(email, pass)) {
                login(email, pass)
            } else {
                Toast.makeText(this, "Correo electrónico o contraseña no válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, pass: String) {
        usersProvider.login(email, pass)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d("MainActivity","Response: ${response.body()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    // Inicio de sesión exitoso
                    //val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    //startActivity(intent)
                    //finish()
                    Toast.makeText(this@MainActivity,response.body()?.message,Toast.LENGTH_LONG).show()
                    SaveUserInsesion(response.body()?.data.toString())
                } else {
                    // Error en el inicio de sesión
                    Toast.makeText(this@MainActivity, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                // Error de red u otro error
                Toast.makeText(this@MainActivity, "Error de conexión, por favor intenta de nuevo, Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun SaveUserInsesion(data:String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPref.save(key = "user",user)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Inicio de sesión exitoso, puedes usar la cuenta
            // Aquí puedes actualizar la UI con la información de la cuenta
            val email = account?.email
            val displayName = account?.displayName
            // etc.
        } catch (e: ApiException) {
            // La firma de inicio de sesión falló, maneja el error
            e.printStackTrace()
        }
    }

    fun btnRegister(viewRegister: TextView) {
        viewRegister.setOnClickListener {
            val i = Intent(this, Register::class.java)
            startActivity(i)
        }
    }

    // Validar correo electrónico
    fun String.validarEmail(email: String): Boolean {
        val EMAIL_REGEX = Regex("[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|outlook\\.com|microsoft\\.com|zoho\\.com|fastmail\\.com)")
        return EMAIL_REGEX.matches(email)
    }

    // Validar contraseña
    fun String.validarPass(pass: String): Boolean {
        val PASSWORD_REGEX = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*()\\-_=+,.?\":;<>|\\[\\]{}~]).{8,}$")
        return PASSWORD_REGEX.matches(pass)
    }

    private fun isValid(email: String, pass: String): Boolean {
        var isValid = true

        if (email.isBlank()) {
            inputLayoutEmail?.error = "Campo obligatorio"
            isValid = false
        } else if (!email.validarEmail(email)) {
            inputLayoutEmail?.error = "Email no válido"
            isValid = false
        } else {
            inputLayoutEmail?.isErrorEnabled = false
        }

        if (pass.isBlank()) {
            inputLayoutPass?.error = "Campo obligatorio"
            isValid = false
        } else if (!pass.validarPass(pass)) {
            inputLayoutPass?.error = "Contraseña no válida"
            isValid = false
        } else {
            inputLayoutPass?.isErrorEnabled = false
        }

        return isValid
    }
}
