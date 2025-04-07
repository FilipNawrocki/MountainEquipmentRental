package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pl.example1.mountainequipmentrental.R

class RegistrationViews : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration_views)

        auth = FirebaseAuth.getInstance()

        val emailTXT = findViewById<TextView>(R.id.TextEmailRegistration)
        val passTXT = findViewById<TextView>(R.id.TextPasswordRegistration)
        val registerBnt = findViewById<Button>(R.id.buttonRegister)

        registerBnt.setOnClickListener {
            val email = emailTXT.text.toString()
            val password = passTXT.text.toString()
            registerUser(email, password)
        }

    }

    private fun registerUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                   val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Toast.makeText(this, "Verification email sent to $email", Toast.LENGTH_SHORT).show()
                                //finish()
                            } else {
                                Toast.makeText(this, "Failed to send verification email: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }


                }else{
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }

            }

    }
}