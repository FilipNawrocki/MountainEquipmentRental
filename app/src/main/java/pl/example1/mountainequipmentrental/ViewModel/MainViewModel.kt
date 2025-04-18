package pl.example1.mountainequipmentrental.ViewModel

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import pl.example1.mountainequipmentrental.Model.CategoriesModel
import pl.example1.mountainequipmentrental.Model.GearModel
import pl.example1.mountainequipmentrental.Model.RentalModel
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import android.content.Context as context


class MainViewModel: ViewModel() {
    private val database = Firebase.firestore


    private val _sprzetList = MutableLiveData<List<CategoriesModel>>()
    val sprzetList: LiveData<List<CategoriesModel>> get() = _sprzetList

    // CategoriesList

    fun loadCategories() {
        database.collection("Categories").get().addOnSuccessListener { result ->
            val list = result.map { document ->
                document.toObject(CategoriesModel::class.java).apply { }
            }
            _sprzetList.value = list
        }
            .addOnFailureListener {
                Log.e("SprzetViewModel", "Błąd ładowania sprzętu", it)
            }
    }

    // EquipmentList

    private val _gearList = MutableLiveData<List<GearModel>>()
    val gearList: LiveData<List<GearModel>> get() = _gearList

    fun loadAvailableGear(category: String, fromDate: String, toDate: String) {
        database.collection("Gear")
            .whereEqualTo("categoryName", category)
            .get()
            .addOnSuccessListener { gearDocs ->

                val gearModels = gearDocs.map { document ->
                    document.toObject(GearModel::class.java).apply {
                        id = document.id
                    }

                }
                val availableGear = mutableListOf<GearModel>()

                val total = gearModels.size
                var checked = 0

                for (gear in gearModels) {
                    database.collection("Rental")
                        .whereEqualTo("gearId", gear.id)
                        .get()
                        .addOnSuccessListener { rentalDocs ->
                            val hasConflict = rentalDocs.any { rentalDoc ->
                                val rental = rentalDoc.toObject(RentalModel::class.java)
                                !rental.returned && datesOverlap(
                                    fromDate,
                                    toDate,
                                    rental.dateFrom,
                                    rental.dateTo
                                )
                            }

                            if (!hasConflict) {
                                availableGear.add(gear)
                            }

                            checked++
                            if (checked == total) {
                                _gearList.value = availableGear
                            }
                        }
                }


                if (gearModels.isEmpty()) {
                    _gearList.value = emptyList()
                }
            }
    }

    private fun datesOverlap(from1: String, to1: String, from2: String, to2: String): Boolean {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start1 = LocalDate.parse(from1, format)
        val end1 = LocalDate.parse(to1, format)
        val start2 = LocalDate.parse(from2, format)
        val end2 = LocalDate.parse(to2, format)

        return !(end1.isBefore(start2) || start1.isAfter(end2))
    }

    fun isAvailable( gearId: String, fromDate: String, toDate: String, context: context, name: String, surname: String, telephone_number: String) {
        database.collection("Rental")
            .whereEqualTo("gearId", gearId)
            .get()
            .addOnSuccessListener { gearDocs ->
                val gearRent = gearDocs.map { document ->
                    document.toObject(RentalModel::class.java).apply { }

                }
                for (gear in gearRent){
                    var hasConflict = datesOverlap(fromDate, toDate, gear.dateFrom, gear.dateTo)
                    if (hasConflict){
                        Toast.makeText(context, "Ten sprzęt już jest wypożyczony na ten okres", Toast.LENGTH_SHORT).show()
                    }
                    if (!hasConflict){
                        val rentalModel = RentalModel(
                            gearId = gearId,
                            dateFrom = fromDate,
                            dateTo = toDate,
                            Name = name,
                            Surname = surname,
                            telephone_number = telephone_number,
                            returned = false,
                            email = CurrentUser.email.toString())
                        rentGear(rentalModel)
                    }
                }

            }

    }


    fun loadGear() {
        database.collection("Gear").get().addOnSuccessListener { result ->
            val list = result.map { document ->
                document.toObject(GearModel::class.java).apply { id = document.id }
            }
            _gearList.value = list
        }
    }

    fun saveGearToFirebase(context: context, id: String, name: String, description: String, category: String, priceText: String, isAvailable: Boolean) {

        if (id.isEmpty() || name.isEmpty() || description.isEmpty() || category.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(context, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull()
        if (price == null) {
            Toast.makeText(context, "Niepoprawna cena", Toast.LENGTH_SHORT).show()
            return
        }

        val gear = GearModel(
            id = id,
            name = name,
            description = description,
            available = isAvailable,
            pricePerWeek = price,
            categoryName = category
        )

        database.collection("Gear").document(id)
            .set(gear)
            .addOnSuccessListener {
                Toast.makeText(context, "Dodano sprzęt", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Błąd zapisu: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    // Rent

    fun rentGear(rentalModel: RentalModel) {
        val rentalRef = Firebase.firestore.collection("Rental")

        rentalRef.add(rentalModel)
            .addOnSuccessListener { documentReference ->
                Log.d("RentalViewModel", "Wypożyczenie zapisane z ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("RentalViewModel", "Błąd zapisu wypożyczenia", e)
            }
    }

    //Gear Delete

    fun deleteGear(gearId: String, context: context ) {
        database.collection("Gear").document(gearId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Sprzęt usunięty", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Błąd usuwania: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    //QR Code

    fun generateQrCode(text: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val bmp = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    fun saveBitmapToGallery(context: context, bitmap: Bitmap) {
        val filename = "QR_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.png"
        val fos: OutputStream?

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QR_Codes_MountainEquApp")
        }

        val imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { context.contentResolver.openOutputStream(it) }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(context, "Zapisano kod QR w galerii 🎉", Toast.LENGTH_LONG).show()
        }
    }


}


