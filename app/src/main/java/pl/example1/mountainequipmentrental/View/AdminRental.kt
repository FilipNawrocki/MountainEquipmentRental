package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import pl.example1.mountainequipmentrental.R

class AdminRental : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_rental)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Button

        findViewById<Button>(R.id.Wypożycz_Bnt).setOnClickListener {
            navController.navigate(R.id.categoryEquipmentFragment)
        }

        findViewById<Button>(R.id.Edytuj_Bnt).setOnClickListener {
            navController.navigate(R.id.rentalAdminEditFragment)
        }

        findViewById<Button>(R.id.QR_Bnt).setOnClickListener {
            navController.navigate(R.id.qrScanFragment)
        }
    }
}
