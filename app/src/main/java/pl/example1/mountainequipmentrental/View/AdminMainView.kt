package pl.example1.mountainequipmentrental.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.example1.mountainequipmentrental.R

class AdminMainView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin_main_view)


        val wypozyczalniaBnt = findViewById<Button>(R.id.Wypozyczalnia_Bnt)

        wypozyczalniaBnt.setOnClickListener {
            val intent = Intent(this, AdminRental::class.java)
            startActivity(intent)
        }

    }
}