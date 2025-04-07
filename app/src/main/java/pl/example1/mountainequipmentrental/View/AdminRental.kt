package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import pl.example1.mountainequipmentrental.R

class AdminRental : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_rental)

        // Jeśli chcesz później reagować na kliknięcia przycisków:
        // Można nawigować przez NavController np.:

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Przyciski
        findViewById<Button>(R.id.Wypożycz_Bnt).setOnClickListener {
            navController.navigate(R.id.categoryEquipmentFragment)
        }

        findViewById<Button>(R.id.Edytuj_Bnt).setOnClickListener {
            // Tutaj dodaj inną akcję z nav_graph jeśli chcesz
        }

        findViewById<Button>(R.id.QR_Bnt).setOnClickListener {
            // Tutaj też np. nawigacja do innego fragmentu
        }
    }
}
