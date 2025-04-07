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
}