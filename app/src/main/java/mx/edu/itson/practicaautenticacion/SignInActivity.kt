package mx.edu.itson.practicaautenticacion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.edtEmailR)
        val password: EditText = findViewById(R.id.edtPasswordR)
        val passwordConfirm: EditText = findViewById(R.id.edtConfirmPassword)
        val error: TextView = findViewById(R.id.txtErrorR)

        val boton: Button = findViewById(R.id.btnRegister)

        error.visibility = View.INVISIBLE

        boton.setOnClickListener{
            if (email.text.isEmpty() || password.text.isEmpty() || passwordConfirm.text.isEmpty()){
                error.text = "Todos los campos deben de ser llenados."
                error.visibility = View.VISIBLE
            }
            else if (!password.text.toString().equals(passwordConfirm.text.toString())){
                error.text = "Las contraseÃ±as no coinciden."
                error.visibility = View.VISIBLE
            }
            else{
                error.visibility = View.INVISIBLE
                singIn(email.text.toString(), password.text.toString())
            }
        }
    }

    fun singIn(email: String, password: String){
        Log.d("INFO", "email: ${email}, password: ${password}")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                Log.d("INFO", "singInWithEmail:succes")
                val user = auth.currentUser
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            else{
                Log.w("ERROR", "singInWithEmail:failure", task.exception)
                Toast.makeText(baseContext,"El registro fallo!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}