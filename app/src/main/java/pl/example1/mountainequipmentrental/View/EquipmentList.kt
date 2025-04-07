package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import pl.example1.mountainequipmentrental.Adapter.GearAdapter
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.FragmentEquipmentListBinding

class EquipmentList : Fragment() {
    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val adapter = GearAdapter(mutableListOf()) { gear ->
            // kliknięcie w sprzęt
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.gearList.observe(viewLifecycleOwner) { listaSprzetu ->
            adapter.updateList(listaSprzetu)
        }

// Ładuj dostępny sprzęt:
        val args = EquipmentListArgs.fromBundle(requireArguments())
        val categoryName = args.categoryName
        val dateFrom = args.dateFrom
        val dateTo = args.dateTo
        viewModel.loadAvailableGear(categoryName, dateFrom, dateTo)

        // Debug print
        Toast.makeText(requireContext(),"Kategoria: $categoryName, Od: $dateFrom, Do: $dateTo",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
