package pl.example1.mountainequipmentrental.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pl.example1.mountainequipmentrental.R

class MainActivity : AppCompatActivity() {

    private lateinit var loginBnt: Button
    private lateinit var registerBnt: TextView
    private lateinit var email: TextView
    private lateinit var password: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.TextEmail)
        password = findViewById(R.id.TextPassword)
        loginBnt = findViewById(R.id.SingInButton)
        registerBnt = findViewById(R.id.RegistrationButton)

        loginBnt.setOnClickListener {
            val emailtxt = email.text.toString()
            val passwordtxt = password.text.toString()
            loginUser(emailtxt, passwordtxt)
        }

        registerBnt.setOnClickListener {
            startActivity(Intent(this, RegistrationViews::class.java))
        }

    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AdminMainView::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Please verify your email first.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}