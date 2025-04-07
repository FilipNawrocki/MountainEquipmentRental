package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.databinding.FragmentEquipmentListBinding

class EquipmentList : Fragment() {



    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var categoryName: String? =

        // Debug print
        //Log.d("EquipmentList", "Kategoria: $categoryName, Od: $dateFrom, Do: $dateTo")

        // Tutaj możesz wczytać sprzęt na podstawie tych danych
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
