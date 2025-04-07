package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.example1.mountainequipmentrental.Adapter.CategoriesAdapter
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.FragmentCategoryEquipmentBinding
import pl.example1.mountainequipmentrental.View.CategoryEquipmentFragmentDirections
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CategoryEquipmentFragment : Fragment() {

    private var _binding: FragmentCategoryEquipmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoriesAdapter
    private lateinit var mainViewModel : MainViewModel

    var selectedStartDate: String? = null
    var selectedEndDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.calendarView1.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedStartDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        }
        binding.calendarView2.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedEndDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        }
        categoryAdapter = CategoriesAdapter(emptyList(), onItemClick = {
            // kliknięcie
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = categoryAdapter

        mainViewModel.loadCategories()
        mainViewModel.sprzetList.observe(viewLifecycleOwner) { listaSprzetu ->
            categoryAdapter = CategoriesAdapter(listaSprzetu, onItemClick = {listaSprzetu ->
                mainViewModel.setCategory(listaSprzetu)

            })
            binding.recyclerView.adapter = categoryAdapter
        }

        binding.searchBnt.setOnClickListener {
            val category = mainViewModel.getCategory()
            val startDate = selectedStartDate
            val endDate = selectedEndDate

            if (category != null && startDate != null && endDate != null) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val start = LocalDate.parse(startDate, formatter)
                val end = LocalDate.parse(endDate, formatter)

                // Sprawdzenie, czy data końcowa jest wcześniejsza
                if (end.isBefore(start)) {
                    Toast.makeText(requireContext(), "Data końcowa nie może być wcześniejsza niż początkowa!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                val action = CategoryEquipmentFragmentDirections
                    .actionCategoryEquipmentFragmentToEquipmentList(
                        categoryName = category.Name,
                        dateFrom = startDate,
                        dateTo = endDate
                    )
                findNavController().navigate(action)
            } else {
                // Wyświetl komunikat, że coś nie zostało wybrane
                Toast.makeText(requireContext(), "Wybierz kategorię i daty!", Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}