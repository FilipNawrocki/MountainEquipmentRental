package pl.example1.mountainequipmentrental.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import pl.example1.mountainequipmentrental.Model.CategoriesModel
import pl.example1.mountainequipmentrental.Model.GearModel
import pl.example1.mountainequipmentrental.Model.RentalModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel: ViewModel() {
    private lateinit var category : CategoriesModel
    private val database = Firebase.firestore


    private val _sprzetList = MutableLiveData<List<CategoriesModel>>()
    val sprzetList: LiveData<List<CategoriesModel>> get() = _sprzetList

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

    fun setCategory(category: CategoriesModel) {
        this.category = category
    }
    fun getCategory() = category

   // EquipmentList

    private val _gearList = MutableLiveData<List<GearModel>>()
    val gearList: LiveData<List<GearModel>> get() = _gearList

    fun loadAvailableGear(category: String, fromDate: String, toDate: String) {
        database.collection("Gear")
            .whereEqualTo("CategoryName", category)
            .get()
            .addOnSuccessListener { gearDocs ->
                val gearList = mutableListOf<GearModel>()

                val gearModels = gearDocs.map { document ->
                    document.toObject(GearModel::class.java).apply {
                        id = document.id // Przypisanie ID dokumentu z Firestore
                    }

                }

                // Sprawdź dostępność każdego sprzętu
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
                                !rental.returned && datesOverlap(fromDate, toDate, rental.dateFrom, rental.dateTo)
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

                // Obsługa przypadku, gdy nie ma żadnych sprzętów
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
}