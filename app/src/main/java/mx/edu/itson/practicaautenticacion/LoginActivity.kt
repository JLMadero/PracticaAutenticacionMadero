package mx.edu.itson.practicaautenticacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.edtEmail)
        val password: EditText = findViewById(R.id.edtPassword)
        val error: TextView = findViewById(R.id.txtError)

        val boton: Button = findViewById(R.id.btnLogin)
        val botonRegister: Button = findViewById(R.id.btnGoRegister)

        error.visibility = View.INVISIBLE

        boton.setOnClickListener{
            login(email.text.toString(), password.text.toString())
        }

        botonRegister.setOnClickListener{
            goToRegister()
        }

    }

    public override fun onStart(){
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser != null){
            goToMain(currentUser)
        }
    }

    fun goToRegister(){
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    fun goToMain(user: FirebaseUser){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", user.email)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun showError(text: String = "", visible: Boolean){
        val error: TextView = findViewById(R.id.txtError)
        error.text = text
        error.visibility = if(visible) View.VISIBLE else View.INVISIBLE

    }

    fun login(email: String, password: String){
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            // Mostrar mensaje de error al usuario
            showError("El usuario y/o contraseña no pueden estar vacios", true)
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                val user = auth.currentUser
                showError(visible = false)
                goToMain(user!!)
            }
            else{
                showError("Usuario y/o contraseña incorrectos", true)
            }
        }

    }

}