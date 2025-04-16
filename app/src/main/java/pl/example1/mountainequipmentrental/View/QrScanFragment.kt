package pl.example1.mountainequipmentrental.View

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import pl.example1.mountainequipmentrental.R

class QrScanFragment : Fragment() {

    private lateinit var barcodeView: CompoundBarcodeView
    private val db = FirebaseFirestore.getInstance()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) barcodeView.resume()
        else Toast.makeText(requireContext(), "Brak uprawnień do aparatu", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_qr_scan, container, false)
        barcodeView = view.findViewById(R.id.barcode_scanner)
        barcodeView.decodeContinuous(callback)
        return view
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            barcodeView.resume()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            val scannedId = result.text
            barcodeView.pause()

            db.collection("equipment").document(scannedId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val isAvailable = doc.getBoolean("isAvailable") ?: true
                        if (isAvailable) {
                            db.collection("equipment").document(scannedId)
                                .update("isAvailable", false)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Sprzęt wypożyczony ✅", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            Toast.makeText(requireContext(), "Sprzęt już wypożyczony ❌", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Nie znaleziono sprzętu o ID: $scannedId", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Błąd połączenia z bazą danych", Toast.LENGTH_LONG).show()
                }
        }

        override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {}
    }
}
